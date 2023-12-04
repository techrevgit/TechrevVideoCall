package com.example.techrevvideocallplugin;

import android.content.Context
import android.media.AudioManager
import com.twilio.audioswitch.AudioDevice
import com.twilio.audioswitch.AudioDeviceChangeListener
import com.twilio.audioswitch.AudioSwitch

private const val TAG = "LMDAudioSwitch"


class LMDAudioSwitch {

    var audioSwitch: AudioSwitch ? = null


    constructor(
            context: Context
    ) {
        audioSwitch = AudioSwitch(context, audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            // Do something with audio focus change

        }
        );
    }

    internal val deviceConnectionListener = object : AudioDeviceChangeListener {
        override fun invoke(audioDevices: List<AudioDevice>, selectedAudioDevice: AudioDevice?) {
            chooseDevice(selectedAudioDevice)
        }
    }
    fun start()
   {
       audioSwitch?.start(deviceConnectionListener)

   }   
    
    /*
         * Audio management
         */
private  fun chooseDevice(audioDevice : AudioDevice?): Boolean
    {
        var isselected =false
        if (audioDevice?.name === "Bluetooth") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true
        } else if (audioDevice?.name === "Wired Headset") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true

        } else if (audioDevice?.name === "Earpiece") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true
        } else if (audioDevice?.name === "Speakerphone") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true

        }
        return isselected
}
    private  fun chooseDeviceForVideoCall(audioDevice : AudioDevice?): Boolean
    {
        var isselected =false
        if (audioDevice?.name === "Bluetooth") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true
        } else if (audioDevice?.name === "Wired Headset") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true

        } else if (audioDevice?.name === "Speakerphone") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true

        }
        else if (audioDevice?.name === "Earpiece") {
            audioSwitch?.selectDevice(audioDevice)
            isselected=true
        }
        return isselected
    }
     fun selectDevice() {
        var audioDeviceList = audioSwitch?.availableAudioDevices
         if (audioDeviceList != null) {
             for (audioDevice in audioDeviceList) {
                 if(chooseDevice(audioDevice))
                 {
                     activate();
                     break;
                 }
             }
         }

    }

    fun selectDeviceForVideoCall() {
        var audioDeviceList = audioSwitch?.availableAudioDevices
        if (audioDeviceList != null) {
            for (audioDevice in audioDeviceList) {
                if(chooseDeviceForVideoCall(audioDevice))
                {
                    activate();
                    break;
                }
            }
        }

    }
 fun activate()
 {
     audioSwitch?.activate()
 }
    fun deactivate()
    {
        audioSwitch?.deactivate()
    }
fun stop()
{
    try {
        audioSwitch?.stop()
    } catch (e: Exception) {
    }
}
}
