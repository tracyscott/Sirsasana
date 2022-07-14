package art.lookingup.sirsasana.patterns;

import heronarts.lx.LX;
import heronarts.lx.pattern.LXPattern;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.LXParameterListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility base class for patterns that are based on parametric animation phases.
 */
public abstract class AnimT extends LXPattern {
  private static final Logger logger = Logger.getLogger(AnimT.class.getName());
  protected float time = 0.0f;
  protected float curPhaseLength = 1.0f;

  int curAnimPhase;
  float curAnimPhaseLocalT;

  List<String> phaseNames = new ArrayList<String>();
  List<CompoundParameter> phaseRatios = new ArrayList<CompoundParameter>();
  List<Float> normalizedPhaseT = new ArrayList<>();
  CompoundParameter tParam = new CompoundParameter("t", 0.0f, 0.0f, 1.0f);
  float totalPhaseRatios = 0.0f;

  public AnimT(LX lx) {
    super(lx);
    addParameter("t", tParam);
  }

  /**
   * Registers an animation phase.
   * @param phaseName The name of the phase.
   * @param phaseRatio The default relative length of the phase.  A knob will be created for tuning.
   * @param maxPhaseRatio The maximum relative phase length.
   * @param description Description to be shown for the ratio knob.
   */
  protected void registerPhase(String phaseName, float phaseRatio, float maxPhaseRatio, String description) {
    phaseNames.add(phaseName);
    CompoundParameter p = new CompoundParameter(phaseName, phaseRatio, 0.1f, maxPhaseRatio).setDescription(description);
    phaseRatios.add(p);
    addParameter(p);
    p.addListener(new LXParameterListener() {
      public void onParameterChanged(LXParameter parameter) {
        recomputeRatios();
      }
    });
    recomputeRatios();
  }

  public void recomputeRatios() {
    totalPhaseRatios = 0f;
    for (CompoundParameter cp : phaseRatios) {
      totalPhaseRatios += cp.getValuef();
    }
    normalizedPhaseT = new ArrayList<Float>(phaseRatios.size());
    for (CompoundParameter cp : phaseRatios) {
      normalizedPhaseT.add(cp.getValuef() / totalPhaseRatios);
    }
  }


  /**
   * Given a globalT between 0.0 and 1.0, figure out our curAnimPhase and compute the phase-local T value.
   * @param globalT
   */
  public void setCurPhaseAndLocalT(float globalT) {
    float accumulatedT = 0.0f;
    float prevAccumulatedT = 0.0f;
    int phaseNum = 0;
    for (Float f : normalizedPhaseT) {
      accumulatedT += f;
      if (globalT <= accumulatedT) {
        curAnimPhase = phaseNum;
        curAnimPhaseLocalT = (globalT - prevAccumulatedT)/f;
        return;
      }
      phaseNum++;
      prevAccumulatedT = accumulatedT;
    }
  }

  public String getCurrentPhaseName() {
    if (curAnimPhase < phaseNames.size())
      return phaseNames.get(curAnimPhase);
    else
      return "";
  }

  public void onActive() {
    time = 0;
  }

  public void run(double deltaMs) {
    setCurPhaseAndLocalT(tParam.getValuef());
    renderPhase(curAnimPhase, curAnimPhaseLocalT);
  }

  abstract public void renderPhase(int curAnimPhase, float phaseLocalT);

}
