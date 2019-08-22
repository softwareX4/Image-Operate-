package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Main extends Application {
    public static String mainViewID = "MainView";
    public static String mainViewRes = "/resource/show.fxml";
    public static String secondViewID = "SecondView";
    public static String secondViewRes = "/resource/second.fxml";
    public static String thirdViewID = "ThirdView";
    public static String thirdViewRes = "/resource/third.fxml";
    private StageController stageController ;


    // the main stage
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{


        try
    {/*
        // load the FXML resource
        FXMLLoader loader = new FXMLLoader(getClass().getResource("show.fxml"));
        BorderPane root = (BorderPane) loader.load();
        // set a whitesmoke background
        root.setStyle("-fx-background-color: whitesmoke;");
        Scene scene = new Scene(root, 1050, 800);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        // create the stage with the given title and the previously created
        // scene
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Image operate");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        // init the controller
        Controller controller = loader.getController();
        controller.setStage(this.primaryStage);
        controller.init();

        */




        stageController = new StageController();
        stageController.setPrimaryStage("primaryStage",primaryStage);
        stageController.loadStage(mainViewID,mainViewRes);
        stageController.loadStage(secondViewID,secondViewRes);
        stageController.loadStage(thirdViewID,thirdViewRes);
        stageController.setStage(mainViewID);

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    }


    public static void main(String[] args) {




        // load the native OpenCV library
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       // System.out.println(System.getProperty("java.library.path"));
        launch(args);
        }

    static {
        try {

            // getClass().getClassLoader().getResourceAsStream(arg0);
            // System.out.println("MainTest.class.getClass()"+MainTest.class.getClass().toString());
            InputStream in = Main.class
                    .getResourceAsStream("/resource/opencv_java410.dll");

            System.out.println("Main.class.getClass()" + in);

            File ffile = new File("");
            String filePath = null;
            filePath = ffile.getAbsolutePath() + File.separator
                    + "opencv_java410.dll";
            System.out.println("filePath opencv_java410.dll is " + filePath);
            File dll = new File(filePath);
            FileOutputStream out = new FileOutputStream(dll);

            int i;
            byte[] buf = new byte[1024];
            try {
                while ((i = in.read(buf)) != -1) {
                    out.write(buf, 0, i);
                }
            } finally {
                in.close();
                out.close();
            }
            System.load(dll.getAbsolutePath());//
            dll.deleteOnExit();

        } catch (Exception e) {
            System.err.println("load jni error!");
        }
    }
}
