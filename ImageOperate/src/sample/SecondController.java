package sample;

import com.sun.javafx.geom.Vec3f;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import static org.opencv.core.Core.*;
import static org.opencv.core.Mat.zeros;
import static org.opencv.imgproc.Imgproc.*;

public class SecondController extends AbstractController implements ControlledStage,Initializable {
    @FXML
    private Slider brightBar;
    @FXML
    private VBox barVbox;
    @FXML
    private Slider contrastBar;
    @FXML
    private ImageView IV00;
    @FXML
    private ImageView IV10;
    @FXML
    private ImageView IV01;
    @FXML
    private ImageView IV11;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button back;

    @FXML
    private Button SmoothnessBlur;
    @FXML
    private Button SmoothnessGBlur;
    @FXML
    private Button SmoothnessBBlur;
    @FXML
    private Button SmoothnessMBlur;
    @FXML
    private Button EnhanceLog;
    @FXML
    private Button EnhanceLapras;
    @FXML
    private Button EnhanceGamma;
    @FXML
    private Button ShapenSobel;
    @FXML
    private Button ShapenLapras;
    @FXML
    private Button Histogram;


    // the main stage
    private Stage stage;
    // the JavaFX file chooser
    private FileChooser fileChooser;
    // support variables
    private Mat image;
    private Mat grayImg;
    private List<Mat> planes;
    // support variables
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
        this.grayImg = new Mat();
        this.planes = new ArrayList<>();
        this.gridPane.setDisable(true);
        this.barVbox.setDisable(true);

        this.image = new Mat();
        this.img01 = new Mat();
        this.img10 = new Mat();
        this.img11 = new Mat();


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
            this.gridPane.setDisable(false);
            this.barVbox.setDisable(false);
            g_srcImage = image;
            if(!this.planes.isEmpty()){
                this.planes.clear();
            }
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

    public void OnEnhanceLog(ActionEvent actionEvent) {

        Mat out = new Mat(image.size(), CvType.CV_32FC(3));

        double []clone;
        for (int i = 0; i < image.rows(); i++)
        {
            for (int j = 0; j < image.cols(); j++)
            {

                clone = out.get(i,j).clone();

                    clone[0] = Math.log(1 + image.get(i,j).clone()[0]);
                    clone[1] = Math.log(1 + image.get(i,j).clone()[1]);
                    clone[2] = Math.log(1 + image.get(i,j).clone()[2]);
                    out.put(i,j,clone);
            }
        }
        Core.normalize(out, out, 0, 255, NORM_MINMAX);
        convertScaleAbs(out, out);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
    }

    public void OnEnhanceLapras(ActionEvent actionEvent) {

        Mat out = new Mat();
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
        float[] data = new float[]{0,-1,0,0, 5, 0,0, -1, 0};
        //kernel.put(3,3,new float[]{0, -1, 0, 0, 5, 0, 0, -1, 0});
        kernel.put(0,0,data);
        Imgproc.filter2D(image, out, CvType.CV_8UC3, kernel);

        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out;

    }

    public void OnEnhanceGamma(ActionEvent actionEvent) {
        Mat out = new Mat(image.size(), CvType.CV_32FC3);
        double []clone;
        for (int i = 0; i < image.rows(); i++)
        {
            for (int j = 0; j < image.cols(); j++)
                {
                clone = out.get(i,j).clone();
                clone[0] = (image.get(i,j).clone()[0])*(image.get(i,j).clone()[0])*(image.get(i,j).clone()[0]);
                clone[1] = (image.get(i,j).clone()[1])*(image.get(i,j).clone()[1])*(image.get(i,j).clone()[1]);
                clone[2] = (image.get(i,j).clone()[2])*(image.get(i,j).clone()[2])*(image.get(i,j).clone()[2]);

                out.put(i,j,clone);
            }
        }
        normalize(out, out, 0, 255, NORM_MINMAX);
        convertScaleAbs(out, out);    //ax +b
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = out;

    }


    public void OnHistogram(ActionEvent actionEvent) {
        List<Mat>imageRGB = new ArrayList<>();
        Mat out = new Mat();
        split(image, imageRGB);
        for (int i = 0; i < 3; i++)
        {
            equalizeHist(imageRGB.get(i), imageRGB.get(i));
        }
        merge(imageRGB, out);
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = out;
    }

    public void OnSmoothnessBlur(ActionEvent actionEvent) {

        Mat out  = image.clone();
        out = salt( out,30000);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
        Mat out2 = new Mat();
        blur(out,out2, new Size(3,3));
        this.updateImageView(IV11,Utils.mat2Image(out2));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out2;
    }

    public void OnSmoothnessGBlur(ActionEvent actionEvent) {

        Mat out  = image.clone();
        out = salt( out,30000);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
        Mat out2 = new Mat();
        GaussianBlur(out,out2, new Size(3,3),0,0);
        this.updateImageView(IV11,Utils.mat2Image(out2));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out2;
    }

    public void OnSmoothnessBBlur(ActionEvent actionEvent) {

        Mat out  = image.clone();
        out = salt( out,30000);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        Mat out2 = new Mat();
        bilateralFilter(out,out2, 9,75,75);
        this.updateImageView(IV11,Utils.mat2Image(out2));
        this.IV11.setFitWidth(FIX_WIDTH);

    }

    public void OnSmoothnessMBlur(ActionEvent actionEvent) {
        Mat out  = image.clone();
        out = salt( out,30000);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
        Mat out2 = new Mat();
        medianBlur(out,out2, 3);
        this.updateImageView(IV11,Utils.mat2Image(out2));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out2;
    }

    public void OnShapenSobel(ActionEvent actionEvent) {
        Mat out = new Mat(), sobel = new Mat();
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        GaussianBlur(image, image, new Size(3, 3), 0, 0, BORDER_DEFAULT);
        cvtColor(image, sobel, COLOR_BGR2GRAY);

        this.updateImageView(IV01,Utils.mat2Image(sobel));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = sobel;
        Mat grad_x= new Mat(), grad_y= new Mat();
        Mat abs_grad_x= new Mat(), abs_grad_y= new Mat();
        Sobel(sobel, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT);
        Sobel(sobel, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(grad_x, abs_grad_x);
        convertScaleAbs(grad_y, abs_grad_y);
        addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, out);
        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out;
    }

    public void OnShapenLapras(ActionEvent actionEvent) {
        Mat lapras = new Mat(), gray = new Mat();
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        int kernel_size = 3;
        GaussianBlur(image, image,new Size(3, 3), 0, 0, BORDER_DEFAULT);
        cvtColor(image, lapras, COLOR_BGR2GRAY);

        this.updateImageView(IV01,Utils.mat2Image(lapras));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = lapras;
        Mat out = new Mat();
        Laplacian(lapras, gray, ddepth, kernel_size, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(gray, out);
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = out;
    }


    public static Mat salt(Mat image, int n) {
        // 随机数生成对象
        Random random = new Random();
        //图像通道
        int nbChannels = image.channels();

        double []clone = new double[3];
        //加盐数量
        for (int i = 0; i < n; i++) {
            /**
             * 获取随机行、列
             * 噪点随机分布
             */
            int row = random.nextInt(image.rows());
            int col = random.nextInt(image.cols());
            //处理全部通道数据，均进行加盐，设置为最大值255
            for (int channel = 0; channel < nbChannels; channel++) {
                //indexer.put(row, col, channel, 255);
                clone[channel] = 255;

            }
            image.put(row,col,clone);
        }
        return image;
    }


    double g_nContrastValue;
    double g_nBrightValue;
    Mat g_srcImage, g_dstImage;

    public void OnContrastAndBright(MouseEvent mouseEvent) {

        g_dstImage = zeros(g_srcImage.size(), g_srcImage.type());
        g_nContrastValue = contrastBar.getValue();
        g_nBrightValue = brightBar.getValue();
        double[]clone;
        for (int y = 0; y < g_srcImage.rows(); y++) {
            for (int x = 0; x < g_srcImage.cols(); x++) {
                clone = g_dstImage.get(y,x).clone();
                for (int c = 0; c < 3; c++) {
                    clone[c] = (g_nContrastValue*0.01)*(g_srcImage.get(y,x).clone()[c])+ g_nBrightValue;
                }
                g_dstImage.put(y,x,clone);
            }
        }


        this.updateImageView(IV10,Utils.mat2Image(g_dstImage));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = g_dstImage;

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
        myController.setStage(Main.mainViewID,Main.secondViewID);
    }
}
