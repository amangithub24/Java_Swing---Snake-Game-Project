package project.factory;

import project.music.PlayMusic;

public class Factory 
{
  public static PlayMusic getPlayMusic()
   {
     return new PlayMusic();
   }
}
