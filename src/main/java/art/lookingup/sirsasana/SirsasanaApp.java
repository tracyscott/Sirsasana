/**
 * Copyright 2020- Mark C. Slee, Heron Arts LLC
 *
 * This file is part of the LX Studio software library. By using
 * LX, you agree to the terms of the LX Studio Software License
 * and Distribution Agreement, available at: http://lx.studio/license
 *
 * Please note that the LX license is not open-source. The license
 * allows for free, non-commercial use.
 *
 * HERON ARTS MAKES NO WARRANTY, EXPRESS, IMPLIED, STATUTORY, OR
 * OTHERWISE, AND SPECIFICALLY DISCLAIMS ANY WARRANTY OF
 * MERCHANTABILITY, NON-INFRINGEMENT, OR FITNESS FOR A PARTICULAR
 * PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package art.lookingup.sirsasana;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.*;

import art.lookingup.ui.*;
import com.google.common.reflect.ClassPath;
import heronarts.lx.LX;
import heronarts.lx.LXPlugin;
import heronarts.lx.effect.LXEffect;
import heronarts.lx.model.LXModel;
import heronarts.lx.pattern.LXPattern;
import heronarts.lx.studio.LXStudio;
import processing.core.PApplet;

/**
 * This is an example top-level class to build and run an LX Studio
 * application via an IDE. The main() method of this class can be
 * invoked with arguments to either run with a full Processing 4 UI
 * or as a headless command-line only engine.
 */
public class SirsasanaApp extends PApplet implements LXPlugin {

  private static int pixelDensity = 1;

  private static final String WINDOW_TITLE = "Sirsasana";

  private static int WIDTH = 1280;
  private static int HEIGHT = 800;
  private static boolean FULLSCREEN = false;
  private static final String LOG_FILENAME_PREFIX = "sirsasana";
  private static final int MAX_LOG_AGE_DAYS = 2;
  private static final int MAX_LOG_AGE_SECS = MAX_LOG_AGE_DAYS * 24 * 60 * 60;

  public static UIPixliteConfig pixliteConfig;

  static public float[] panelPosParams;

  static {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%3$s: %1$tc [%4$s] %5$s%6$s%n");
  }

  /**
   * Set the main logging level here.
   *
   * @param level the new logging level
   */
  public static void setLogLevel(Level level) {
    // Change the logging level here
    Logger root = Logger.getLogger("");
    root.setLevel(level);
    for (Handler h : root.getHandlers()) {
      h.setLevel(level);
    }
  }


  /**
   * Adds logging to a file. The file name will be appended with a dash, date stamp, and
   * the extension ".log".
   *
   * @param prefix prefix of the log file name
   * @throws IOException if there was an error opening the file.
   */
  public static void addLogFileHandler(String prefix) throws IOException {
    deleteOldLogs(prefix, MAX_LOG_AGE_SECS);
    Logger root = Logger.getLogger("");
    String suffix = "" + System.currentTimeMillis() / 1000;
    // Human readable version, but harder to clean up old logs:
    // suffix = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
    Handler h = new FileHandler(prefix + "-" + suffix + ".log");
    h.setFormatter(new SimpleFormatter());
    root.addHandler(h);
  }

  public static void deleteOldLogs(String prefix, int seconds) throws IOException {
    File logDir = new File(".");
    for (final File fileEntry: logDir.listFiles()) {
      if (fileEntry.getName().endsWith(".log")) {
        try {
          if (fileEntry.getName().startsWith("sirsasana")) {
            String[] parts1 = fileEntry.getName().split("-");
            String[] parts2 = parts1[1].split("\\.");
            int time = Integer.parseInt(parts2[0]);
            if (time < System.currentTimeMillis()/1000 - seconds) {
              logger.info("Cleaning old log file: " + fileEntry.getName());
              fileEntry.delete();
              // Clean up .lck file also
              File lckFile = new File(fileEntry.getAbsolutePath() + ".lck");
              lckFile.delete();
            } else {
              logger.info("Keeping log file: " + fileEntry.getName());
            }
          }
        } catch (Exception ex) {
          logger.info("Bad log file name, skipping cleanup: " + fileEntry.getName());
        }
      }
    }
  }

  private static final Logger logger = Logger.getLogger(SirsasanaApp.class.getName());

  @Override
  public void settings() {
    if (FULLSCREEN) {
      fullScreen(PApplet.P3D);
    } else {
      size(WIDTH, HEIGHT, PApplet.P3D);
    }
    pixelDensity(pixelDensity);
  }

  @Override
  public void setup() {
    LXStudio.Flags flags = new LXStudio.Flags(this);
    flags.resizable = true;
    flags.useGLPointCloud = false;
    flags.startMultiThreaded = true;

    try {
      addLogFileHandler(LOG_FILENAME_PREFIX);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Error creating log file: " + LOG_FILENAME_PREFIX, ex);
    }

    logger.info("Current renderer:" + sketchRenderer());
    logger.info("Current graphics:" + getGraphics());
    logger.info("Current graphics is GL:" + getGraphics().isGL());
    loadModelParams();
    LXModel model = SirsasanaModel.createModel();
    new LXStudio(this, flags, model);
    this.surface.setTitle(WINDOW_TITLE);
  }

  public void loadModelParams() {
  }

  @Override
  public void initialize(LX lx) {
    // Here is where you should register any custom components or make modifications
    // to the LX engine or hierarchy. This is also used in headless mode, so note that
    // you cannot assume you are working with an LXStudio class or that any UI will be
    // available.
    registerAll(lx);
  }

  public void initializeUI(LXStudio lx, LXStudio.UI ui) {
    // Here is where you may modify the initial settings of the UI before it is fully
    // built. Note that this will not be called in headless mode. Anything required
    // for headless mode should go in the raw initialize method above.
  }

  public void onUIReady(LXStudio lx, LXStudio.UI ui) {
    // At this point, the LX Studio application UI has been built. You may now add
    // additional views and components to the Ui heirarchy.
    pixliteConfig = (UIPixliteConfig) new UIPixliteConfig(ui, lx).setExpanded(false).addToContainer(lx.ui.leftPane.global);
    Output.configurePixliteOutput(lx);
    logger.info("Model bounds: " + lx.getModel().xMin + "," + lx.getModel().yMin + " to " + lx.getModel().xMax + "," + lx.getModel().yMax);
  }

  @Override
  public void draw() {
    // All handled by core LX engine, do not modify, method exists only so that Processing
    // will run a draw-loop.
  }

  /**
   * Main interface into the program. Two modes are supported, if the --headless
   * flag is supplied then a raw CLI version of LX is used. If not, then we embed
   * in a Processing 3 applet and run as such.
   *
   * @param args Command-line arguments
   */
  public static void main(String[] args) {
    LX.log("Initializing LX version " + LXStudio.VERSION);
    boolean headless = false;
    File projectFile = null;
    //FULLSCREEN = true;
    for (int i = 0; i < args.length; ++i) {
      if ("--help".equals(args[i]) || "-h".equals(args[i])) {
      } else if ("--headless".equals(args[i])) {
        headless = true;
      } else if ("--fullscreen".equals(args[i]) || "-f".equals(args[i])) {
        FULLSCREEN = true;
      } else if ("--width".equals(args[i]) || "-w".equals(args[i])) {
        try {
          WIDTH = Integer.parseInt(args[++i]);
        } catch (Exception x ) {
          LX.error("Width command-line argument must be followed by integer");
        }
      } else if ("--height".equals(args[i]) || "-h".equals(args[i])) {
        try {
          HEIGHT = Integer.parseInt(args[++i]);
        } catch (Exception x ) {
          LX.error("Height command-line argument must be followed by integer");
        }
      } else if (args[i].endsWith(".lxp")) {
        try {
          projectFile = new File(args[i]);
        } catch (Exception x) {
          LX.error(x, "Command-line project file path invalid: " + args[i]);
        }
      }
    }
    if (headless) {
      // We're not actually going to run this as a PApplet, but we need to explicitly
      // construct and set the initialize callback so that any custom components
      // will be run
      LX.Flags flags = new LX.Flags();
      flags.initialize = new SirsasanaApp();
      if (projectFile == null) {
        LX.log("WARNING: No project filename was specified for headless mode!");
      }
      LX.headless(flags, projectFile);
    } else {
      String[] newArgs = new String[2];
      String[] sketchArgs = {"--density=" + 2, "art.lookingup.sirsasana.SirsasanaApp"};
      File hdpiFlag = new File("hdpi");
      if (hdpiFlag.exists())
        pixelDensity = 2;
      PApplet.main(concat(sketchArgs, args));
      //PApplet.runSketch(sketchArgs, null);
    }
  }


  /**
   * Registers all patterns and effects that LX doesn't already have registered.
   * This check is important because LX just adds to a list.
   *
   * @param lx the LX environment
   */
  private void registerAll(LX lx) {
    List<Class<? extends LXPattern>> patterns = lx.registry.patterns;
    List<Class<? extends LXEffect>> effects = lx.registry.effects;
    final String parentPackage = getClass().getPackage().getName();

    try {
      ClassPath classPath = ClassPath.from(getClass().getClassLoader());
      for (ClassPath.ClassInfo classInfo : classPath.getAllClasses()) {
        // Limit to this package and sub-packages
        if (!classInfo.getPackageName().startsWith(parentPackage)) {
          continue;
        }
        Class<?> c = classInfo.load();
        if (Modifier.isAbstract(c.getModifiers())) {
          continue;
        }
        if (LXPattern.class.isAssignableFrom(c)) {
          Class<? extends LXPattern> p = c.asSubclass(LXPattern.class);
          if (!patterns.contains(p)) {
            lx.registry.addPattern(p);
            logger.info("Added pattern: " + p);
          }
        } else if (LXEffect.class.isAssignableFrom(c)) {
          Class<? extends LXEffect> e = c.asSubclass(LXEffect.class);
          if (!effects.contains(e)) {
            lx.registry.addEffect(e);
            logger.info("Added effect: " + e);
          }
        }
      }
    } catch (IOException ex) {
      logger.log(Level.WARNING, "Error finding pattern and effect classes", ex);
    }
  }
}
