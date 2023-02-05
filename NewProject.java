import com.example.augustino.DS.Course;
import com.example.augustino.DS.Projects;
import com.example.augustino.DS.Student;
import com.example.augustino.HelloApplication;
import com.example.augustino.HibBontrol.CourseHib;
import com.example.augustino.HibBontrol.ProjectHib;
import com.example.augustino.HibBontrol.UserHib;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewProject implements Initializable {
    public TextArea descProjectField;
    public TextField nameProjectField;
    public ComboBox respUserBox;

    private int courseId;
    private int parentId;
    private int userId;
    private int projectId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SystemCourse");
    UserHib userHib = new UserHib(entityManagerFactory);
    CourseHib courseHib = new CourseHib(entityManagerFactory);
    ProjectHib projectHib = new ProjectHib(entityManagerFactory);

    //pagrindinis metodo veikimas

    public void doProject(ActionEvent actionEvent) throws IOException {
        if (parentId != 0) {
            Projects parent = projectHib.getProjectById(parentId);
            Projects projects = new Projects(nameProjectField.getText(), descProjectField.getText(), parent,
                    (Student) userHib.getUserById(Integer.parseInt(respUserBox.getValue().toString().split("\\ | ") [0])) ,
                    userHib.getUserById(userId));
            parent.getSubProjects().add(projects);
            projectHib.editProject(parent);
        } else if (courseId != 0) {
            Course course = courseHib.getCourseById(courseId);
            Projects projects = new Projects(nameProjectField.getText(), descProjectField.getText(),
                    (Student) userHib.getUserById(Integer.parseInt(respUserBox.getValue().toString().split("\\ | ")[0])) ,
                    userHib.getUserById(userId), course);
            course.getCourseProjects().add(projects);
            courseHib.editCourse(course);
        } else {
            Projects projects = projectHib.getProjectById(projectId);
            projects.setProjectName(nameProjectField.getText());
            projects.setProjectDesc(descProjectField.getText());
            projectHib.editProject(projects);
        }
        loadScreen();
    }

    //užkrauna pasirinktą langą

    public void loadScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainCourse.fxml"));
        Parent root = fxmlLoader.load();

        MainCourse mainCourse = fxmlLoader.getController();
        mainCourse.setMainCourse(userId);

        Scene scene = new Scene(root);

        Stage stage = (Stage) nameProjectField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //sudeda naujo kurso duomenis

    public void newCourseData(boolean modif,int courseId, int parentId, int userId, int projectId) {
        this.courseId = courseId;
        this.parentId = parentId;
        this.userId = userId;
        this.projectId = projectId;
        if (modif) {
            loadNewProject();
        }
    }

    //užkrauna nauują projektą

    private void loadNewProject() {
        Projects projects = projectHib.getProjectById(projectId);
        nameProjectField.setText(projects.getProjectName());
        descProjectField.setText(projects.getProjectDesc());
    }

    //užkrauna studentų sąrašą

    @Override
    public void initializeUsers(URL url, ResourceBundle resourceBundle) {
        List<Student> studentUsers = userHib.getAllStudent();
        studentUsers.forEach(temp -> respUserBox.getItems().add(temp.getId() + " | " + temp.getName()));
    }
}
