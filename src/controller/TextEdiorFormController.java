package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class TextEdiorFormController {


    public Button btnNewFile;
    public TextArea txtArea;
    public AnchorPane mainContext;
    private String filePath = "";  //stores opened file path
    Stage stage;


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

        int index=txtArea.getCaretPosition();
        txtArea.setText(txtArea.getText().replace(txtArea.getSelectedText(),""));
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
            }else{
                stage = (Stage) mainContext.getScene().getWindow();
                stage.setTitle( file.getName());
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
        txtArea.selectAll();;
    }


}
