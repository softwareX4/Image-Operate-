package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Math.abs;
import static org.opencv.imgproc.Imgproc.*;

public class Controller extends AbstractController   implements ControlledStage,Initializable {

    StageController myController;

    @FXML
    private ImageView IV00;
    @FXML
    private ImageView IV10;
    @FXML
    private ImageView IV01;
    @FXML
    private ImageView IV11;

    @FXML
    private Button gray;
    @FXML
    private Button Dilate;
    @FXML
    private Button Erode;
    @FXML
    private Button Blur;
    @FXML
    private Button fourier;
    @FXML
    private Button antifourier;


    @FXML
    private MenuButton Morphology;
    @FXML
    private Button loadImg;
    @FXML
    private MenuItem open;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem morGrad;
    @FXML
    private MenuItem topHat;
    @FXML
    private MenuItem blackHat;
    @FXML
    private MenuItem morDilate;
    @FXML
    private MenuItem morErode;
    @FXML
    private TextField rotateText;
    @FXML
    private Button rotate;
    @FXML
    private CheckBox check;
    @FXML
    private Button translate;
    @FXML
    private Button resize;
    @FXML
    private TextField x_translate;
    @FXML
    private TextField y_translate;
    @FXML
    private TextField x_resize;
    @FXML
    private TextField y_resize;

    @FXML
    private  Button trans1;
    @FXML
    private  Button trans2;





    // the main stage
    private Stage stage;
    // the JavaFX file chooser
    private FileChooser fileChooser;
    // support variables
    private Mat image;
    private Mat fourierImg;
    private List<Mat> planes;
    // the final complex image
    private Mat complexImage;

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
        this.fourierImg = new Mat();

        this.img01 = new Mat();
        this.img10 = new Mat();
        this.img11 = new Mat();
        this.planes = new ArrayList<>();
        this.complexImage = new Mat();
        this.gray.setDisable(true);
        this.Dilate.setDisable(true);
        this.Erode.setDisable(true);
        this.Blur.setDisable(true);
        this.Morphology.setDisable(true);
        this.fourier.setDisable(true);
        this.antifourier.setDisable(true);
        this.translate.setDisable(true);
        this.rotate.setDisable(true);
        this.resize.setDisable(true);

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


            this.gray.setDisable(false);
            this.Dilate.setDisable(false);
            this.Erode.setDisable(false);
            this.Blur.setDisable(false);
            this.Morphology.setDisable(false);
            this.translate.setDisable(false);
            this.rotate.setDisable(false);
            this.resize.setDisable(false);

            if(!this.planes.isEmpty()){
                this.planes.clear();
            }
            updateImgView();

        }
    }


    public void onGray() {
        Mat out = new Mat();
        Imgproc.cvtColor(this.image,out,Imgproc.COLOR_BGR2GRAY);
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setPreserveRatio(true);
        this.IV10.setFitWidth(FIX_WIDTH);

        Imgproc.cvtColor(this.image,fourierImg,Imgproc.COLOR_BGR2GRAY);
        this.fourier.setDisable(false);
        this.img10 = out;

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


    public void toDilate(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.dilate(this.image, out, element);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;

    }

    public void toErode(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.erode(this.image, out, element);
        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out;
    }

    public void toBlur(ActionEvent actionEvent) {
        Mat out = new Mat();
        Imgproc.blur(this.image,out,new Size(7,7));
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = out;
    }

    public void onOpen(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,MORPH_OPEN, element);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
        this.img01 = out;
    }

    public void onClose(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,Imgproc.MORPH_CLOSE, element);
        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out;
    }

    public void onMorGrad(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,Imgproc.MORPH_GRADIENT, element);
        this.updateImageView(IV01,Utils.mat2Image(out));
        this.IV01.setFitWidth(FIX_WIDTH);
    }

    public void onTopHat(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,Imgproc.MORPH_TOPHAT, element);
        this.updateImageView(IV11,Utils.mat2Image(out));
        this.IV11.setFitWidth(FIX_WIDTH);
        this.img11 = out;
    }

    public void onBlackHat(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,Imgproc.MORPH_BLACKHAT, element);
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = out;
    }

    public void onMorDilate(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,Imgproc.MORPH_DILATE, element);
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
    }

    public void onMorErode(ActionEvent actionEvent) {
        Mat out = new Mat();
        Mat element = Imgproc.getStructuringElement(MORPH_RECT, new Size(15,15));
        Imgproc.morphologyEx(this.image, out,Imgproc.MORPH_ERODE, element);
        this.updateImageView(IV10,Utils.mat2Image(out));
        this.IV10.setFitWidth(FIX_WIDTH);
        this.img10 = out;
    }


    //-------------------------------transform------------------------------------
    //旋转后大小不变

    public static Mat rotate(Mat src, double angele) {
        Mat dst = src.clone();
        Point center = new Point(src.width() / 2.0, src.height() / 2.0);
        Mat affineTrans = Imgproc.getRotationMatrix2D(center, angele, 1.0);
        Imgproc.warpAffine(src, dst, affineTrans, dst.size(), Imgproc.INTER_NEAREST);
        return dst;
    }

    public Mat rotate3(Mat splitImage, double angle)
    {
        double thera = angle * Math.PI / 180;
        double a = Math.sin(thera);
        double b = Math.cos(thera);

        int wsrc = splitImage.width();
        int hsrc = splitImage.height();

        int wdst = (int) (hsrc * abs(a) + wsrc * abs(b));
        int hdst = (int) (wsrc * abs(a) + hsrc * abs(b));
        Mat imgDst = new Mat(hdst, wdst, splitImage.type());

        Point pt = new Point(splitImage.cols() / 2, splitImage.rows() / 2);
        // 获取仿射变换矩阵
        Mat affineTrans = Imgproc.getRotationMatrix2D(pt, angle, 1.0);

        System.out.println(affineTrans.dump());
        // 改变变换矩阵第三列的值
        affineTrans.put(0, 2, affineTrans.get(0, 2)[0] + (wdst - wsrc) / 2);
        affineTrans.put(1, 2, affineTrans.get(1, 2)[0] + (hdst - hsrc) / 2);

        Imgproc.warpAffine(splitImage, imgDst, affineTrans, imgDst.size(),
                Imgproc.INTER_CUBIC | Imgproc.WARP_FILL_OUTLIERS);
        return imgDst;
    }

    public void OnRotate(ActionEvent actionEvent) {
        String text = rotateText.getText().trim();
        Mat out;
        if(!text.isEmpty()){
            if(check.isSelected()){
                out  = rotate3(this.image,Double.parseDouble(text));
            }
            else {
                out = rotate(this.image,Double.parseDouble(text));
            }
            this.updateImageView(IV10,Utils.mat2Image(out));
            this.IV10.setFitWidth(FIX_WIDTH);
            this.img10 = out;
        }
    }


    //---------------------------------平移-------------------------------------------------

    Mat translateTransform(Mat src, int dx, int dy)//平移后大小不变
    {

        assert (src.depth() == 0);
        int rows = src.rows();
        int cols = src.cols();
        Mat out = new Mat();
        out.create(rows, cols, src.type());
        double []clone;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)//平移后坐标映射到原图像
            {
                clone = out.get(i,j).clone();
                int x = j - dx;
                int y = i - dy;
                //保证映射后的坐标在原图像范围内
                if (x >= 0 && y >= 0 && x < cols && y < rows){
                    clone[0] = src.get(y,x).clone()[0];
                    clone[1] = src.get(y,x).clone()[1];
                    clone[2] = src.get(y,x).clone()[2];
                    out.put(i,j,clone);
                }


            }
        }

        return out;
    }

    Mat translateTransformSize(Mat src, int dx, int dy)//平移后大小变化
    {
        assert (src.depth() == 0);//输出图像的大小
	 int rows = src.rows() + abs(dy);
	 int cols = src.cols() + abs(dx);
        Mat out = new Mat();
        out.create(rows, cols, src.type());
        double []clone;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {

                clone = out.get(i,j).clone();
                int x = j - dx;
                int y = i - dy;
                if (x >= 0 && y >= 0 && x < src.cols() && y < src.rows()){
                    clone[0] = src.get(y,x).clone()[0];
                    clone[1] = src.get(y,x).clone()[1];
                    clone[2] = src.get(y,x).clone()[2];
                    out.put(i,j,clone);
                }

            }
        }
        return out;
    }


    public void OnTranslate(ActionEvent actionEvent) {
        String dx = x_translate.getText().trim();
        String dy = y_translate.getText().trim();
        Mat out;

        if(!(dx.isEmpty() ) || !(dy.isEmpty())){

            if(dx.isEmpty())dx = "0";
            if(dy.isEmpty())dy = "0";
            if(check.isSelected()){
                out  = translateTransformSize(this.image,Integer.parseInt(dx),Integer.parseInt(dy));
            }
            else {
                out = translateTransform(this.image,Integer.parseInt(dx),Integer.parseInt(dy));
            }
            this.updateImageView(IV01,Utils.mat2Image(out));
            this.IV01.setFitWidth(FIX_WIDTH);
            this.img01 = out;
        }
    }
//-----------------------------------resize-----------------------------------

    public void OnResize(ActionEvent actionEvent) {

        String x = x_resize.getText().trim();
        String y = y_resize.getText().trim();
        Mat out = new Mat();
        if(!(x.isEmpty() ) && !(y.isEmpty())){
            double dx = Double.parseDouble(x);
            double dy = Double.parseDouble(y);
            dx *= image.cols();
            dy *= image.rows();
            Size dsize = new Size( dx, dy);

            Imgproc.resize(this.image,out,dsize,0,0,Imgproc.INTER_AREA);
            this.updateImageView(IV11,Utils.mat2Image(out));
            this.img11 = out;



            // 创建新的stage
            Stage secondStage = new Stage();
            secondStage.setTitle("原始图像");
            StackPane secondPane = new StackPane();
            ImageView img = new ImageView();
            secondPane.getChildren().add(img);
            this.updateImageView(img,Utils.mat2Image(this.image));
            Scene secondScene = new Scene(secondPane, this.image.cols(),this.image.rows());
            secondStage.setScene(secondScene);
            secondStage.show();
            // 创建新的stage
            Stage thirdStage = new Stage();
            thirdStage.setTitle("缩放后");
            StackPane thirdPane = new StackPane();
            ImageView img2 = new ImageView();
            thirdPane.getChildren().add(img2);
            this.updateImageView(img2,Utils.mat2Image(out));
            Scene thirdScene = new Scene(thirdPane, dx, dy);
            thirdStage.setScene(thirdScene);
            thirdStage.show();



        }

    }

//---------------------------------Fourier------------------------------------

    /**
     * The action triggered by pushing the button for apply the dft to the
     * loaded image
     */
    @FXML
    protected void OnFourier()
    {
        // optimize the dimension of the loaded image
        Mat padded = this.optimizeImageDim(this.fourierImg);
        padded.convertTo(padded, CvType.CV_32F);
        // prepare the image planes to obtain the complex image
        this.planes.add(padded);
        this.planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        // prepare a complex image for performing the dft
        Core.merge(this.planes, this.complexImage);

        // dft
        Core.dft(this.complexImage, this.complexImage);

        // optimize the image resulting from the dft operation
        Mat magnitude = this.createOptimizedMagnitude(this.complexImage);

        // show the result of the transformation as an image
        this.updateImageView(IV11, Utils.mat2Image(magnitude));
        // set a fixed width
        this.IV11.setFitWidth(FIX_WIDTH);
        // preserve image ratio
        this.IV11.setPreserveRatio(true);

        // enable the button for performing the antitransformation
        this.antifourier.setDisable(false);
        // disable the button for applying the dft
        this.fourier.setDisable(true);
        this.img11 = magnitude;
    }

    /**
     * The action triggered by pushing the button for apply the inverse dft to
     * the loaded image
     */
    @FXML
    protected void OnAntiFourier()
    {
        Core.idft(this.complexImage, this.complexImage);

        Mat restoredImage = new Mat();
        Core.split(this.complexImage, this.planes);
        Core.normalize(this.planes.get(0), restoredImage, 0, 255, Core.NORM_MINMAX);

        // move back the Mat to 8 bit, in order to proper show the result
        restoredImage.convertTo(restoredImage, CvType.CV_8U);

        this.updateImageView(IV11, Utils.mat2Image(restoredImage));
        // set a fixed width
        this.IV11.setFitWidth(FIX_WIDTH);
        // preserve image ratio
        this.IV11.setPreserveRatio(true);

        // disable the button for performing the antitransformation
        this.antifourier.setDisable(true);
        this.img11 = restoredImage;
    }

    /**
     * Optimize the image dimensions
     *
     * @param image
     *            the {@link Mat} to optimize
     * @return the image whose dimensions have been optimized
     */
    private Mat optimizeImageDim(Mat image)
    {
        // init
        Mat padded = new Mat();
        // get the optimal rows size for dft
        int addPixelRows = Core.getOptimalDFTSize(image.rows());
        // get the optimal cols size for dft
        int addPixelCols = Core.getOptimalDFTSize(image.cols());
        // apply the optimal cols and rows size to the image
        Core.copyMakeBorder(image, padded, 0, addPixelRows - image.rows(), 0, addPixelCols - image.cols(),
                Core.BORDER_CONSTANT, Scalar.all(0));

        return padded;
    }

    /**
     * Optimize the magnitude of the complex image obtained from the DFT, to
     * improve its visualization
     *
     * @param complexImage
     *            the complex image obtained from the DFT
     * @return the optimized image
     */
    private Mat createOptimizedMagnitude(Mat complexImage)
    {
        // init
        List<Mat> newPlanes = new ArrayList<>();
        Mat mag = new Mat();
        // split the comples image in two planes
        Core.split(complexImage, newPlanes);
        // compute the magnitude
        Core.magnitude(newPlanes.get(0), newPlanes.get(1), mag);

        // move to a logarithmic scale
        Core.add(Mat.ones(mag.size(), CvType.CV_32F), mag, mag);
        Core.log(mag, mag);
        // optionally reorder the 4 quadrants of the magnitude image
        this.shiftDFT(mag);
        // normalize the magnitude image for the visualization since both JavaFX
        // and OpenCV need images with value between 0 and 255
        // convert back to CV_8UC1
        mag.convertTo(mag, CvType.CV_8UC1);
        Core.normalize(mag, mag, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

        // you can also write on disk the resulting image...
        // Imgcodecs.imwrite("../magnitude.png", mag);

        return mag;
    }

    /**
     * Reorder the 4 quadrants of the image representing the magnitude, after
     * the DFT
     *
     * @param image
     *            the {@link Mat} object whose quadrants are to reorder
     */
    private void shiftDFT(Mat image)
    {
        image = image.submat(new Rect(0, 0, image.cols() & -2, image.rows() & -2));
        int cx = image.cols() / 2;
        int cy = image.rows() / 2;

        Mat q0 = new Mat(image, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(image, new Rect(cx, 0, cx, cy));
        Mat q2 = new Mat(image, new Rect(0, cy, cx, cy));
        Mat q3 = new Mat(image, new Rect(cx, cy, cx, cy));

        Mat tmp = new Mat();
        q0.copyTo(tmp);
        q3.copyTo(q0);
        tmp.copyTo(q3);

        q1.copyTo(tmp);
        q2.copyTo(q1);
        tmp.copyTo(q2);
    }

    public void OnTrans1(ActionEvent actionEvent) throws Exception {
       /* Second second = new Second();
        second.showWindow();*/
       myController.setStage(Main.secondViewID,Main.mainViewID);

    }

    public void OnTrans2(ActionEvent actionEvent) throws Exception {
       /* Third third = new Third();
        third.showWindow();*/

        myController.setStage(Main.thirdViewID,Main.mainViewID);
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

    @Override
    public void setStageController(StageController stageController) {
        this.myController = stageController;
    }
}
