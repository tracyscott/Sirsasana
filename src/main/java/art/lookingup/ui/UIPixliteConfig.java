package art.lookingup.ui;

import art.lookingup.sirsasana.Output;
import heronarts.lx.LX;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.studio.LXStudio;

public class UIPixliteConfig extends UIConfig {
  public static final String PIXLITE_IP = "pxlt_ip";
  public static final String PIXLITE_PORT = "pxlt_port";

  public static final String title = "pixlite IP";
  public static final String filename = "pixliteconfig.json";
  public LX lx;
  private boolean parameterChanged = false;

  public UIPixliteConfig(final LXStudio.UI ui, LX lx) {
    super(ui, title, filename);
    this.lx = lx;

    registerStringParameter(PIXLITE_IP, "127.0.0.1");
    registerStringParameter(PIXLITE_PORT, "6454");

    save();

    buildUI(ui);
  }

  public String icecreamPixliteIp() {
    return getStringParameter(PIXLITE_IP).getString();
  }

  public int icecreamPixlitePort() {
    return Integer.parseInt(getStringParameter(PIXLITE_PORT).getString());
  }

  @Override
  public void onParameterChanged(LXParameter p) {
    parameterChanged = true;
  }

  @Override
  public void onSave() {
    if (parameterChanged) {
      Output.restartOutput(lx);
      parameterChanged = false;
    }
  }
}
