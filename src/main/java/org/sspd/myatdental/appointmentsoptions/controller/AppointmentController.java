package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class AppointmentController implements Initializable {

    @FXML
    private TextArea addresstxt;

    @FXML
    private TextField agetxt;

    @FXML
    private DatePicker appdatebox;

    @FXML
    private TextArea appnotetxt;

    @FXML
    private TextArea apppurposetxt;

    @FXML
    private ComboBox<String> appstatusbox;

    @FXML
    private TextField apptimetxt;

    @FXML
    private DatePicker dateofbirthbox;

    @FXML
    private ComboBox<String> doctorlistconbo;

    @FXML
    private JFXCheckBox femalecheck;

    @FXML
    private JFXCheckBox malecheck;

    @FXML
    private TextArea medicaltxt;

    @FXML
    private TextField patientnametxt;

    @FXML
    private TextField phonetxt;

    @FXML
    private ComboBox<String> townshipconbo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        townshipFilter();



    }

    private void townshipFilter(){
        townshipconbo.setEditable(true);


        townshipconbo.setItems(townshiplist());


        townshipconbo.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object == null ? "" : object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });


        TextField editor = townshipconbo.getEditor();
        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                townshipconbo.setItems(townshiplist());
            } else {
                String filter = newValue.toLowerCase();
                ObservableList<String> filteredList = townshiplist().stream()
                        .filter(township -> township.toLowerCase().contains(filter))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                townshipconbo.setItems(filteredList);
                townshipconbo.show();
            }
        });
    }

    private ObservableList<String> townshiplist(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Ahlone");
        list.add("Bahan");
        list.add("Botahtaung");
        list.add("Dagon");
        list.add("Dagon Seikkan");
        list.add("Dala");
        list.add("Dawbon");
        list.add("East Dagon");
        list.add("Hlaing");
        list.add("Hlaingthaya");
        list.add("Insein");
        list.add("Kamayut");
        list.add("Kyauktada");
        list.add("Kyimyindaing");
        list.add("Lanmadaw");
        list.add("Latha");
        list.add("Mayangone");
        list.add("Mingala Taungnyunt");
        list.add("Mingaladon");
        list.add("North Dagon");
        list.add("North Okkalapa");
        list.add("Pabedan");
        list.add("Pazundaung");
        list.add("Sanchaung");
        list.add("Seikkyi Kanaungto");
        list.add("Shwepyitha");
        list.add("South Dagon");
        list.add("South Okkalapa");
        list.add("Tamwe");
        list.add("Thaketa");
        list.add("Thingangyun");
        list.add("Yankin");
        list.add("Kyeemyindaing");


        return list;
    }
}
