module com.tde.listasnaolinearescodigomorse {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tde.listasnaolinearescodigomorse to javafx.fxml;
    exports com.tde.listasnaolinearescodigomorse;
}