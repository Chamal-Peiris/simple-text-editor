package controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Index;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEdiorFormController {


    public Button btnNewFile;
    public TextArea txtArea;
    public AnchorPane mainContext;
    public JFXTextField txtSearchText;
    public Button btnFindDown;
    public Button btnFindUp;
    public JFXToggleButton btnRegex;
    public JFXToggleButton btnCaseSensitive;
    public JFXTextField txtReplaceText;
    public Button btnReplace;
    public Button btnReplaceAll;
    public Label lblFindCount;
    public Label lblTotalWordCount;
    public MenuItem menFind;
    public MenuItem menFindAll;
    public MenuItem menReplace;
    public MenuItem menReplaceAll;
    private String filePath = "";  //stores opened file path
    Stage stage;
    private boolean textChanged = false;
    private Matcher matcher;
    private final List<Index> searchList = new ArrayList();
    private int searchCount = 0;


    public void initialize() {


        txtArea.textProperty().addListener((observable, oldValue, newValue) -> {
            textChanged = true;

            Pattern pattern = Pattern.compile("\\s");
            Matcher matcher = pattern.matcher(txtArea.getText());
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            lblTotalWordCount.setText(String.valueOf(count));
        });
        txtSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtSearchText.getText().isEmpty()) {
                lblFindCount.setText("0");
            }
        });

    }

    public void btnNewfileOnAction(ActionEvent actionEvent) {
        txtArea.clear();
        mainContext.cacheProperty();
        stage = (Stage) mainContext.getScene().getWindow();

        stage.setTitle("Untitled.txt");
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        try {
            saveNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnCutOnAction(ActionEvent actionEvent) {
        cut();
    }

    public void btnCopyOnAction(ActionEvent actionEvent) {
        copy();
    }

    public void btnPasteOnAction(ActionEvent actionEvent) {
        paste();
    }

    public void btnSlectAllAonAction(ActionEvent actionEvent) {
        txtArea.selectAll();
    }

    public void saveNewFile() throws IOException {  // save a file


        stage = (Stage) mainContext.getScene().getWindow();
        if (stage.getTitle().equals("Untitled.txt")) {       //create a new file

            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                File file1 = new File(file.getAbsolutePath() + ".txt");         //check whether the file name exists
                if (file.getName().equals(null)) {

                    new Alert(Alert.AlertType.ERROR, "Please add the file name");

                } else {
                    System.out.println(file.getAbsolutePath() + ".txt");

                    String fileContent = txtArea.getText();
                    byte[] bytes = fileContent.getBytes();

                    FileOutputStream fos = new FileOutputStream(file1);
                    fos.write(bytes);
                    stage.setTitle(file.getName() + ".txt");
                    fos.close();
                }
                new Alert(Alert.AlertType.INFORMATION, "Successfully Saved").show();
            }


        } else {                                                                             //if file already exists
            File file = new File(filePath);
            System.out.println(file.exists());

            String fileContent = txtArea.getText();
            byte[] bytes = fileContent.getBytes();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();

            new Alert(Alert.AlertType.INFORMATION, "Successfully Updated").show();
        }


    }

    public void cut() {       //cut method


        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        String text = txtArea.getSelectedText();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        systemClipboard.setContent(content);

        int index = txtArea.getCaretPosition();
        txtArea.setText(txtArea.getText().replace(txtArea.getSelectedText(), ""));
        txtArea.positionCaret(index);


    }

    public void copy() {


        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        String text = txtArea.getSelectedText();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        systemClipboard.setContent(content);


    }

    private void paste() {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        String text = systemClipboard.getString();
        int index = txtArea.getCaretPosition();
        txtArea.insertText(index, text);
    }

    public void menNewOnAction(ActionEvent actionEvent) {
        txtArea.clear();
        stage = (Stage) mainContext.getScene().getWindow();
        stage.setTitle("Untitled.txt");
    }

    public void menOpenOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        System.out.println("Selected file:" + file.getAbsolutePath());
        filePath = file.getAbsolutePath();

        try {
            FileInputStream fis = new FileInputStream(file);

            byte[] fileBytes = new byte[fis.available()];

            fis.read(fileBytes);
            String fileContent = new String(fileBytes);
            new Alert(Alert.AlertType.INFORMATION, "File Successfully Opened").show();
            txtArea.setText(fileContent);
            stage = (Stage) mainContext.getScene().getWindow();
            stage.setTitle(file.getName());

            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtArea.textProperty().addListener((observable, oldValue, newValue) -> {       //add * when editing
            if (newValue != null) {
                stage = (Stage) mainContext.getScene().getWindow();
                stage.setTitle("*" + file.getName());
            } else {
                stage = (Stage) mainContext.getScene().getWindow();
                stage.setTitle(file.getName());
            }
        });
    }

    public void menSaveOnAction(ActionEvent actionEvent) {
        try {
            saveNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menExitOnAction(ActionEvent actionEvent) {
        if (txtArea.getText().isEmpty()) {
            System.exit(0);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please save file Before You Exit").show();
        }
    }

    public void menCutOnAction(ActionEvent actionEvent) {
        cut();
    }

    public void menCopyOnAction(ActionEvent actionEvent) {
        copy();
    }

    public void menPasteOnAction(ActionEvent actionEvent) {
        paste();
    }

    public void menSelectAllOnAction(ActionEvent actionEvent) {
        txtArea.selectAll();
        ;
    }


    public void btnFindDownOnAction(ActionEvent actionEvent) {
        makeMatcher();
        searchCount++;
    }

    public void btnFindUpOnAction(ActionEvent actionEvent) {
        /*for (Index index:searchList) {
            System.out.println("Start Index"+index.getStartIndex());
            System.out.println("end Index"+index.getEndIndex());
        }*/

        int startIndex = searchList.get(searchCount - 1).getStartIndex();
        int endIndex = searchList.get(searchCount - 1).getEndIndex();

        txtArea.selectRange(startIndex, endIndex);

        searchCount--;

    }

    private void makeMatcher() {  //create matcher object
        txtArea.deselect();
        if (textChanged) {
            int flags = 0;
            if (!btnRegex.isSelected()) flags = flags | Pattern.LITERAL;
            if (!btnCaseSensitive.isSelected()) flags = flags | Pattern.CASE_INSENSITIVE;

            matcher = Pattern.compile(txtSearchText.getText(), flags)
                    .matcher(txtArea.getText());
            textChanged = false;
        }


        if (matcher.find()) {

            txtArea.selectRange(matcher.start(), matcher.end());
            searchList.add(new Index(matcher.start(), matcher.end()));     //adding the indexs to the arraylist

        } else {
            matcher.reset();
        }


    }

    public void btnRegexOnAction(ActionEvent actionEvent) {
        //calling the fire method action listener
        textChanged = true;
        makeMatcher();
    }

    public void btncaseSensitiveOnAction(ActionEvent actionEvent) {
        textChanged = true;
        makeMatcher();
    }

    public void btnReplaceOnAction(ActionEvent actionEvent) {  //replace
        replace();

    }

    private void replace() {
        if (matcher == null) {
            new Alert(Alert.AlertType.ERROR, "Please Insert a search Text First").show();
        } else if (txtReplaceText.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add the text to be replaced with!").show();
        } else {
            txtArea.replaceSelection(txtReplaceText.getText());
            new Alert(Alert.AlertType.INFORMATION, "Replace Succesfully").show();
        }
    }

    public void btnReplaceAllOnaction(ActionEvent actionEvent) {   //replace all
        replaceAll();
    }

    private void replaceAll() {
        if (matcher == null) {
            new Alert(Alert.AlertType.ERROR, "Please Insert a search Text First").show();
        } else if (txtReplaceText.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add the text to be replaced with!").show();
        } else {
            txtArea.setText(matcher.replaceAll(txtReplaceText.getText()));
            new Alert(Alert.AlertType.INFORMATION, "Replace Succesfully").show();
        }
    }

    public void btnFindAllOnAction(ActionEvent actionEvent) {
        findAll();
    }

    private void findAll() {
        if (matcher == null) {
            btnFindDown.fire();
            Pattern pattern = Pattern.compile(txtSearchText.getText(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(txtArea.getText());
            int count = 0;
            while (matcher.find()) {
                count++;
            }

            lblFindCount.setText(String.valueOf(count));
        } else {

            if (txtSearchText.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please add a Text to Search").show();
            } else {
                Pattern pattern = Pattern.compile(txtSearchText.getText(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(txtArea.getText());
                int count = 0;
                while (matcher.find()) {
                    count++;
                }

                lblFindCount.setText(String.valueOf(count));
            }
        }
    }

    public void menFindOnAction(ActionEvent actionEvent) {
        makeMatcher();
    }

    public void menFindAllOnAction(ActionEvent actionEvent) {
        findAll();
    }

    public void menReplaceOnAction(ActionEvent actionEvent) {
        replace();
    }

    public void menReplaceAllOnaction(ActionEvent actionEvent) {
        replaceAll();
    }

    public void menAboutOnAction(ActionEvent actionEvent) throws IOException {
        //navigates within forms in one anchor pain
        URL resource = getClass().getResource("/view/Aboutus.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);

        Stage stage = new Stage();
        stage.setTitle("About Us");
        stage.setScene(scene);
        stage.show();
    }
}
