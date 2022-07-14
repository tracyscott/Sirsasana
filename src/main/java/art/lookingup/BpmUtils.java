package art.lookingup;

import heronarts.lx.LX;

/**
 * Utility class for handling BPM related tasks.
 */
public class BpmUtils {

  /**
   * Scale the speed value by the current BPM setting.  The reference BPM is 120 BPM.
   * @param lx
   * @param value
   * @return
   */
  public float bpmSpeed(LX lx, float value) {
    return value * (float)lx.engine.tempo.bpm() / 120f;
  }

  /**
   * Handle Open Beat Control Master OSC responses.
   */

  public void handleOBCMasterStreamResponse() {

  }
}
