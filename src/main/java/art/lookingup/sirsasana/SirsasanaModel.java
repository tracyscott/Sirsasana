package art.lookingup.sirsasana;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import java.util.*;
import java.util.logging.Logger;

/**
 * Sirsasana Model
 *
 * Lots of floods, some birds.
 *
 */
public class SirsasanaModel extends LXModel {
  private static final Logger logger = Logger.getLogger(SirsasanaModel.class.getName());



  static public LXModel createModel() {
    List<LXPoint> points = new ArrayList<LXPoint>();

    for (int i = 0; i < 10; i++) {
      points.add(new LXPoint(i, 0, 0));
    }
    return new SirsasanaModel(points);
  }


  public SirsasanaModel(List<LXPoint> points) {
    super(points);
  }

}
