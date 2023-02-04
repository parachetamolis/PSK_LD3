package com.example.augustino.fxcontrol;

import com.example.augustino.DS.Company;
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
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class NewProject implements Initializable {
    public TextArea descProjectF;
    public TextArea VardasProjkektasF;
    public TextField VardasProjkektasF;
    public ComboBoxed respUserBox;

    private int courseId;
    private int parentId;
    private int userId;
    private int projectId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SystemCourse");
    UserHib userHib = new UserHib(entityManagerFactory);
    CourseHib courseHib = new CourseHib(entityManagerFactory);
    ProjectHib projectHib = new ProjectHib(entityManagerFactory);

    public void Daritiprojekta(ActionEvent actionEvent) throws IOException{
        if (parentId != 0){
            Projects parent = projectHib.getProjectById(parentId);
            Projects projects = new Projects(VardasProjkektasF.getText(), descProjectF.getText(), parent, (Student) userHib.getUserById(Integer.parseInt(respUserBox.getValue().toString().split("\\ | ") [0])) ,userHib.getUserById(userId));
            parent.getSubProjects().add(projects);
            projectHib.editProject(parent);
        } else if(courseId != 0){
            Course course = courseHib.getCourseById(courseId);
            Projects projects = new Projects(VardasProjkektasF.getText(), descProjectF.getText(), (Student) userHib.getUserById(Integer.parseInt(respUserBox.getValue().toString().split("\\ | ")[0])) ,userHib.getUserById(userId), course);
            course.getCourseProjects().add(projects);
            courseHib.editCourse(course);
        }else{
            Projects projects = projectHib.getProjectById(projectId);
            projects.setProjectName(VardasProjkektasF.getText());
            projects.setProjectDesc(descProjectF.getText());
            projectHib.editProject(projects);
        }
        back();
    }

    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainCourse.fxml"));
        Parent root = fxmlLoader.load();

        MainCourse mainCourse = fxmlLoader.getController();
        mainCourse.setMainCourse(userId);

        Scene scene = new Scene(root);

        Stage stage = (Stage) VardasProjkektasF.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void cancelProject(ActionEvent actionEvent) throws IOException {
        back();
    }

    public void newData(boolean modif,int courseId, int parentId, int userId, int projectId) {
        this.courseId = courseId;
        this.parentId = parentId;
        this.userId = userId;
        this.projectId = projectId;
        if (modif) {
            loadNewProject();
        }
    }

    private void loadNewProject(){
        Projects projects = projectHib.getProjectById(projectId);
        VardasProjkektasF.setText(projects.getProjectName());
        descProjectF.setText(projects.getProjectDesc());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Student> users = userHib.getAllStudent();
        users.forEach(temp -> respUserBox.getItems().add(temp.getId() + " | " + temp.getName()));
    }
}
