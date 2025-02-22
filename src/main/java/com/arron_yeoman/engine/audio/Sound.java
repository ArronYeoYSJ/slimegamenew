package com.arron_yeoman.engine.audio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memFree;

import com.arron_yeoman.engine.io.ResourceLoader;

public class Sound {
    private int bufferID;
    private int sourceID;

    private String fileName;

    private boolean isPlaying = false;
    private boolean loopingSound = false;

    public Sound(String fileName, boolean looping){
        this.fileName = fileName;
        String filepath = ResourceLoader.resourcesAudioString(fileName);
        this.loopingSound = looping;
        
        //allocate space for the buffer
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();

        IntBuffer sampleRateBuffer = stackMallocInt(1);
        
        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null){
            System.err.println("Error: Could not load audio file: " + fileName);
            stackPop();
            stackPop();
            return;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        stackPop();
        stackPop();

        int format = -1;
        if (channels == 1){
            format = AL11.AL_FORMAT_MONO16;
        } else if (channels == 2){
            format = AL11.AL_FORMAT_STEREO16;
        }

        bufferID = AL11.alGenBuffers();
        AL11.alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        sourceID = AL11.alGenSources();
        AL11.alSourcei(sourceID, AL11.AL_BUFFER, bufferID);
        AL11.alSourcef(sourceID, AL11.AL_GAIN, 0.5f);
        AL11.alSourcei(sourceID, AL11.AL_LOOPING, loopingSound ? AL11.AL_TRUE : AL11.AL_FALSE);
        AL11.alSourcef(channels, AL11.AL_POSITION, 0);

        memFree(rawAudioBuffer);
        
    }

    public void delete(){
        AL11.alDeleteSources(sourceID);
        AL11.alDeleteBuffers(bufferID);
    }

    public void play(){
        int state = AL11.alGetSourcei(sourceID, AL11.AL_SOURCE_STATE);
        if (state == AL11.AL_PLAYING){
            AL11.alSourcei(sourceID, AL11.AL_POSITION, 0);
            return;
        }
        AL11.alSourcePlay(sourceID);
        isPlaying = true;
    }

    public void stop(){
        AL11.alSourceStop(sourceID);
        isPlaying = false;
        AL11.alSourcei(sourceID, AL11.AL_POSITION, 0);
    }

    public void pause(){
        AL11.alSourcePause(sourceID);
        isPlaying = false;
    }

    public String getFileName(){
        return this.fileName;
    }

    public boolean isPlaying(){
        int state = AL11.alGetSourcei(sourceID, AL11.AL_SOURCE_STATE);
        if (state == AL11.AL_STOPPED || state == AL11.AL_INITIAL || state == AL11.AL_PAUSED){
            isPlaying = false;
        }
        return isPlaying;
    }
}
