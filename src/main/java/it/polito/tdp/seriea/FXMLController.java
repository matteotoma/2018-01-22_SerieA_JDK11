package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.PuntiStagione;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	txtResult.clear();
    	Team t = boxSquadra.getValue();
    	if(t == null) {
    		txtResult.appendText("Devi selezionare un team!\n");
    		return;
    	}
    	for(PuntiStagione p: model.getpuntiStagione(t))
    		txtResult.appendText(String.format("%s %d \n", p.getS(), p.getPunti()));
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	txtResult.clear();
    	Team t = boxSquadra.getValue();
    	if(t == null) {
    		txtResult.appendText("Devi selezionare un team!\n");
    		return;
    	}
    	model.creaGrafo(t);
    	txtResult.appendText("Grafo creato con "+model.nVertici()+" vertici e "+model.nArchi()+"!\n");
    	PuntiStagione p = model.getAnnataDoro();
    	if(p == null) {
    		txtResult.appendText("errore");
    		return;
    	}
    	txtResult.appendText(String.format("%s %d\n", p.getS(), p.getPunti()));
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	txtResult.clear();
    	List<PuntiStagione> result = model.trovPercorso();
    	for(PuntiStagione p: result)
    		txtResult.appendText(String.format("%s %d \n", p.getS(), p.getPunti()));
    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		setBox();
	}

	private void setBox() {
		this.boxSquadra.getItems().addAll(model.getTeams());
	}
}
