module ru.nsu.ksadov.find.task_2_3_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.ksadov.find.task_2_3_1 to javafx.fxml;
    exports ru.nsu.ksadov.find.task_2_3_1;
}