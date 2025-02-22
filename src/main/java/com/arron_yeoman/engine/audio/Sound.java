package com.arron_yeoman.engine.audio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import org.lwjgl.system.MemoryUtil;

import com.arron_yeoman.engine.io.ResourceLoader;

public class Sound {
    private int bufferID;
    private int sourceID;
    private String fileName;
    private boolean isPlaying = false;
    private boolean loopingSound = false;

    public Sound(String fileName, boolean looping) {
        this.fileName = fileName;
        this.loopingSound = looping;

        // Load the file as an InputStream from inside JAR
        ByteBuffer vorbisData;
        try (InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream("audio/" + fileName)) {
            if (inputStream == null) {
                throw new IOException("Error: Could not load audio file: " + fileName);
            }
            vorbisData = readInputStreamToByteBuffer(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (vorbisData == null || vorbisData.capacity() == 0) {
            System.err.println("ERROR: ByteBuffer is null or empty for file: " + fileName);
            return;
        }

        // Allocate space for the buffer
        IntBuffer error = stackMallocInt(1);
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            System.out.println("Loading sound: " + fileName);
            long vorbis = STBVorbis.stb_vorbis_open_memory(vorbisData, error, null);
            System.out.println("Vorbis: " + vorbis);
            if (vorbis == 0) {
                System.err.println("Error: Could not decode OGG file: " + fileName + " - STB Error Code: " + error.get(0));
                return;
            }

            STBVorbis.stb_vorbis_get_info(vorbis, info);
            int channels = info.channels();
            int sampleRate = info.sample_rate();
            System.err.println("Channels: " + channels + " Sample Rate: " + sampleRate);
            int samples = STBVorbis.stb_vorbis_stream_length_in_samples(vorbis);
            ShortBuffer pcm = MemoryUtil.memAllocShort(samples * channels);
            System.err.println("pcm created");
            STBVorbis.stb_vorbis_get_samples_short_interleaved(vorbis, channels, pcm);
            System.err.println("pcm filled");
            STBVorbis.stb_vorbis_close(vorbis);
            System.out.println("Loaded sound: " + fileName);
            int format = (channels == 1) ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16;

            bufferID = AL11.alGenBuffers();
            AL11.alBufferData(bufferID, format, pcm, sampleRate);

            sourceID = AL11.alGenSources();
            AL11.alSourcei(sourceID, AL11.AL_BUFFER, bufferID);
            AL11.alSourcef(sourceID, AL11.AL_GAIN, 0.5f);
            AL11.alSourcei(sourceID, AL11.AL_LOOPING, loopingSound ? AL11.AL_TRUE : AL11.AL_FALSE);
        }
    }

    private static ByteBuffer readInputStreamToByteBuffer(InputStream inputStream) throws IOException {
        ReadableByteChannel channel = Channels.newChannel(inputStream);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 1024 * 1024); // 4MB buffer
        while (channel.read(buffer) != -1) {}
        buffer.flip();
        return buffer;
    }

    public void delete() {
        AL11.alDeleteSources(sourceID);
        AL11.alDeleteBuffers(bufferID);
    }

    public void play() {
        int state = AL11.alGetSourcei(sourceID, AL11.AL_SOURCE_STATE);
        if (state == AL11.AL_PLAYING) {
            AL11.alSourcei(sourceID, AL11.AL_POSITION, 0);
            return;
        }
        AL11.alSourcePlay(sourceID);
        isPlaying = true;
    }

    public void stop() {
        AL11.alSourceStop(sourceID);
        isPlaying = false;
        AL11.alSourcei(sourceID, AL11.AL_POSITION, 0);
    }

    public void pause() {
        AL11.alSourcePause(sourceID);
        isPlaying = false;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean isPlaying() {
        int state = AL11.alGetSourcei(sourceID, AL11.AL_SOURCE_STATE);
        if (state == AL11.AL_STOPPED || state == AL11.AL_INITIAL || state == AL11.AL_PAUSED) {
            isPlaying = false;
        }
        return isPlaying;
    }
}
