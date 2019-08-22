package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.imgproc.Imgproc.*;

public class ThirdController extends AbstractController  implements ControlledStage ,Initializable{
    @FXML
    private Button sob;
    @FXML
    private Button scha;
    @FXML
    private Button lap;
    @FXML
    private Button can;
    @FXML
    private Button hild;
    @FXML
    private Button back;

    @FXML
    private ImageView IV00;
    @FXML
    private ImageView IV10;
    @FXML
    private ImageView IV01;
    @FXML
    private ImageView IV11;

    // the main stage
    private Stage stage;
    // the JavaFX file chooser
    private FileChooser fileChooser;
    // support variables
    private Mat image;
    private Mat img01;
    private Mat img10;
    private Mat img11;


    private final static int FIX_WIDTH = 350;


    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    @Override
    public void init() {
        this.fileChooser = new FileChooser();
        this.image = new Mat();
        this.img01 = new Mat();
        this.img10 = new Mat();
        this.img11 = new Mat();
        this.sob.setDisable(true);
        this.scha.setDisable(true);
        this.lap.setDisable(true);
        this.can.setDisable(true);
        this.hild.setDisable(true);

    }

    private void updateImgView(){

        this.IV01.setImage(null);
        this.img01 = null;
        this.IV10.setImage(null);
        this.img10 = null;
        this.IV11.setImage(null);
        this.img11 = null;
    }

    @FXML
    public void loadImage(){
        File file = this.fileChooser.showOpenDialog(this.stage);
        if(file != null){
            this.image = Imgcodecs.imread(file.getAbsolutePath());

            this.updateImageView(IV00,Utils.mat2Image(this.image));
            this.IV00.setPreserveRatio(true);
            this.IV00.setFitWidth(FIX_WIDTH);
            this.sob.setDisable(false);
            this.scha.setDisable(false);
            this.lap.setDisable(false);
            this.can.setDisable(false);
            this.hild.setDisable(false);
            updateImgView();


        }
    }


    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view
     *            the {@link ImageView} to update
     * @param image
     *            the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image)
    {
        Utils.onFXThread(view.imageProperty(), image);
    }

    public void OnSobel(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat g_sobelGradient_X = new Mat(), g_sobelGradient_Y = new Mat();
        Mat g_sobelAbsGradient_X = new Mat(), g_sobelAbsGradient_Y = new Mat();
        int g_sobelKernelSize = 1;
        //createTrackbar("参数", WindowName, &g_sobelKernelSize, 3, Sobel);
        Sobel(image, g_sobelGradient_X, CvType.CV_16S, 1, 0, (2 * g_sobelKernelSize + 1), 1, BORDER_DEFAULT);
        convertScaleAbs(g_sobelGradient_X, g_sobelAbsGradient_X);
        Sobel(image, g_sobelGradient_Y, CvType.CV_16S, 0, 1, (2 * g_sobelKernelSize + 1), 1, BORDER_DEFAULT);
        convertScaleAbs(g_sobelGradient_Y, g_sobelAbsGradient_Y);
        addWeighted(g_sobelAbsGradient_X, 0.5, g_sobelAbsGradient_Y, 0.5, 0, out);

        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
    }

    public void OnScharr(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat g_scharrGradient_x = new Mat(), g_scharrGradient_Y = new Mat();
        Mat g_scharrAbsGradient_x = new Mat(), g_scharrAbsGradient_Y = new Mat();
        Scharr(image, g_scharrGradient_x, CvType.CV_16S, 1, 0, 1, 0, BORDER_DEFAULT);
        convertScaleAbs(g_scharrGradient_x, g_scharrAbsGradient_x);
        Scharr(image, g_scharrGradient_Y, CvType.CV_16S, 0, 1, 1, 0, BORDER_DEFAULT);
        convertScaleAbs(g_scharrGradient_Y, g_scharrAbsGradient_Y);
        addWeighted(g_scharrAbsGradient_x, 0.5, g_scharrAbsGradient_Y, 0.5, 0, out);

        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);

        this.img10 = out;
    }

    public void OnLaplace(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat lapras = new Mat(), gray = new Mat();
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        int kernel_size = 3;
        GaussianBlur(image, image, new Size(3, 3), 0, 0, BORDER_DEFAULT);
        cvtColor(image, lapras, COLOR_BGR2GRAY);
        Laplacian(lapras, gray, ddepth, kernel_size, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(gray, out);
        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);

        this.img11 = out;
    }

    public void OnCanny(ActionEvent actionEvent) {
        Mat out = Mat.zeros(image.rows(),image.cols(),0);
        Mat temp = new Mat();
        Mat g_cannyDetectedEdges = new Mat();
        int g_cannyLowThreshold = 3;
        cvtColor(image, temp, COLOR_BGR2GRAY);
        blur(temp, g_cannyDetectedEdges, new Size(3, 3));
        Canny(g_cannyDetectedEdges, g_cannyDetectedEdges, g_cannyLowThreshold, g_cannyLowThreshold * 3, 3);

        image.copyTo(out, g_cannyDetectedEdges);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
    }

    public void OnHildreth(ActionEvent actionEvent) {
        Mat out;
        int kerValue = 9;
        double delta = 1.6;
        int kerLen = kerValue /2;

        Mat kernel = new Mat(kerValue,kerValue, CvType.CV_64F);

        Mat temp = new Mat();
        cvtColor(image, temp, COLOR_BGR2GRAY);

        for (int i = -kerLen; i <= kerLen; i++)
        {
            for (int j = -kerLen; j <= kerLen; j++)
            {
                double res =  (exp(-((pow(j, 2) + pow(i, 2)) / (pow(delta, 2) * 2)))*((pow(j, 2) + pow(i, 2) - 2 * pow(delta, 2)) / (2 * pow(delta, 4))));

                kernel.put(i + kerLen, j + kerLen,res);
            }
        }
        int kerOffset = kerValue / 2;
        Mat laplacian = new Mat(temp.rows() - kerOffset * 2, temp.cols() - kerOffset * 2,CvType.CV_64F);
        out = Mat.zeros(temp.rows() - kerOffset * 2, temp.cols() - kerOffset * 2, temp.type());

        System.out.println(laplacian.channels());
        System.out.println(out.channels());

        double sumLaplacian ;
        for (int i = kerOffset; i < temp.rows() - kerOffset; ++i)
        {
            for (int j = kerOffset; j < temp.cols() - kerOffset; ++j)
            {
                sumLaplacian = 0;
                for (int k = -kerOffset; k <= kerOffset; ++k)
                {
                    for (int m = -kerOffset; m <= kerOffset; ++m)
                    {
                            sumLaplacian += temp.get(i + k, j + m )[0] * kernel.get(kerOffset + k, kerOffset + m)[0];

                    }
                }
                laplacian.put(i - kerOffset, j - kerOffset,sumLaplacian) ;
            }
        }
        for (int y = 1; y < out.rows() - 1; ++y)
        {
            for (int x = 1; x < out.cols() - 1; ++x)
            {
                    if (laplacian.get(y - 1, x)[0]  *  laplacian.get(y + 1, x)[0] < 0)
                    {
                        out.put(y, x,255);
                    }
                    if (laplacian.get(y, x - 1)[0]  *  laplacian.get(y, x + 1)[0] < 0)
                    {
                        out.put(y, x,255);
                    }
                    if (laplacian.get(y + 1, x - 1)[0]  *  laplacian.get(y - 1, x + 1)[0] < 0)
                    {
                        out.put(y, x,255);
                    }
                    if (laplacian.get(y - 1, x - 1)[0]  *  laplacian.get(y + 1, x + 1)[0] < 0)
                    {
                        out.put(y, x,255);
                    }



            }
        }

        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out;
    }

    public void OnClicked(Mat out) {

        if(out == null || out.width()==0)return;
        // 创建新的stage
        Stage secondStage = new Stage();
        StackPane secondPane = new StackPane();
        ImageView img = new ImageView();
        secondPane.getChildren().add(img);
        this.updateImageView(img,Utils.mat2Image(out));
        Scene secondScene = new Scene(secondPane, out.cols(),out.rows());
        secondStage.setScene(secondScene);
        secondStage.show();
    }

    public void OnClicked00(MouseEvent mouseEvent) {
        OnClicked(image);
    }
    public void OnClicked01(MouseEvent mouseEvent) {
        OnClicked(img01);
    }
    public void OnClicked10(MouseEvent mouseEvent) {
        OnClicked(img10);
    }

    public void OnClicked11(MouseEvent mouseEvent) {
        OnClicked(img11);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    StageController myController;
    @Override
    public void setStageController(StageController stageController) {
        this.myController = stageController;
    }

    public void OnBack(ActionEvent actionEvent) {
        myController.setStage(Main.mainViewID,Main.thirdViewID);
    }
}
