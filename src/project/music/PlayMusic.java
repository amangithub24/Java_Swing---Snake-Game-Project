package project.music;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class PlayMusic
{
 public void playMusic(String musicPath)
 {
     InputStream music;
     
     try
     {
         music = new FileInputStream(new File(musicPath));
         AudioStream audio=new AudioStream(music);
         AudioPlayer.player.start(audio);
     }
     catch(Exception e)
     {
         JOptionPane.showMessageDialog(null," AUDIO IS NOT WORKING !!!");
     }
 }

    public void playMusic(KeyCode keyCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
