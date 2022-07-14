package art.lookingup.sirsasana;

import art.lookingup.ui.UIPixliteConfig;
import heronarts.lx.LX;
import heronarts.lx.model.LXPoint;
import heronarts.lx.output.ArtNetDatagram;
import heronarts.lx.output.ArtSyncDatagram;
import heronarts.lx.output.LXDatagram;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Output {
  private static final Logger logger = Logger.getLogger(Output.class.getName());

  public static int artnetPort = 6454;

  static public List<List<LXPoint>> allOutputsPoints = new ArrayList<List<LXPoint>>();

  // We keep track of these so we can restart the network.  There is no way to get a list of children from
  // LXOutputGroup which is the class for lx.engine.output.
  static public List<ArtNetDatagram> outputDatagrams = new ArrayList<ArtNetDatagram>();
  static public LXDatagram artSyncDatagram;


  public static void configureUnityArtNet(LX lx) {
    String unityIpAddress = "127.0.0.1";
    logger.log(Level.INFO, "Using ArtNet: " + unityIpAddress + ":" + artnetPort);

    List<LXPoint> points = lx.getModel().getPoints();
    int numUniverses = (int)Math.ceil(((double)points.size())/170.0);
    logger.info("Num universes: " + numUniverses);
    List<ArtNetDatagram> datagrams = new ArrayList<ArtNetDatagram>();
    int totalPointsOutput = 0;

    for (int univNum = 0; univNum < numUniverses; univNum++) {
      int[] dmxChannelsForUniverse = new int[170];
      for (int i = 0; i < 170 && totalPointsOutput < points.size(); i++) {
        LXPoint p = points.get(univNum*170 + i);
        dmxChannelsForUniverse[i] = p.index;
        totalPointsOutput++;
      }

      ArtNetDatagram artnetDatagram = new ArtNetDatagram(lx, dmxChannelsForUniverse, univNum);
      try {
        artnetDatagram.setAddress(InetAddress.getByName(unityIpAddress)).setPort(artnetPort);
      } catch (UnknownHostException uhex) {
        logger.log(Level.SEVERE, "Configuring ArtNet: " + unityIpAddress, uhex);
      }
      datagrams.add(artnetDatagram);
    }

    for (ArtNetDatagram dgram : datagrams) {
      lx.engine.addOutput(dgram);
    }
  }

  /**
   * TODO: Not designed yet.
   * @param lx
   */
  public static void configurePixliteOutput(LX lx) {
    List<ArtNetDatagram> datagrams = new ArrayList<ArtNetDatagram>();
    String artNetIpAddress = SirsasanaApp.pixliteConfig.getStringParameter(UIPixliteConfig.PIXLITE_IP).getString();
    int artNetIpPort = Integer.parseInt(SirsasanaApp.pixliteConfig.getStringParameter(UIPixliteConfig.PIXLITE_PORT).getString());
    logger.log(Level.INFO, "Using Pixlite ArtNet: " + artNetIpAddress + ":" + artNetIpPort);

  }

  static public void restartOutput(LX lx) {
    boolean originalEnabled = lx.engine.output.enabled.getValueb();
    lx.engine.output.enabled.setValue(false);
    for (ArtNetDatagram dgram : outputDatagrams) {
      lx.engine.output.removeChild(dgram);
    }
    lx.engine.output.removeChild(artSyncDatagram);
    configurePixliteOutput(lx);
    lx.engine.output.enabled.setValue(originalEnabled);
  }
}
