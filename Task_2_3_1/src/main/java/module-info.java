module ru.nsu.ksadov.find.task {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.nsu.ksadov.find.task to javafx.fxml;
    exports ru.nsu.ksadov.find.task;
}