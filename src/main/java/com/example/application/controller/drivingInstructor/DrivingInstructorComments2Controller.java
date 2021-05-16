package com.example.application.controller.drivingInstructor;

import com.example.application.dao.Comment;
import com.example.application.dao.Lesson;
import com.example.application.dao.User;
import com.example.application.dao.UserDetail;
import com.example.application.dao.repo.CommentRepo;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.UserDetailRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DrivingInstructorComments2Controller {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private CommentRepo commentRepo;
    private UserRepo userRepo;
    private UserDetailRepo userDetailRepo;
    private LessonRepo lessonRepo;
    private List<Lesson> lessonList;
    private List<Lesson> lessonForLearnerList;
    private List<Lesson> lessonForLearnerAfterList;
    private List<User> userLearnerList;
    private String error;
    private String news;

    @Autowired
    public DrivingInstructorComments2Controller(HttpSession session, SessionUtil sessionUtil, CommentRepo commentRepo, UserRepo userRepo, UserDetailRepo userDetailRepo, LessonRepo lessonRepo) {
        this.sessionUtil = sessionUtil;
        this.session = session;
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.userDetailRepo = userDetailRepo;
        this.lessonRepo = lessonRepo;
    }

    @GetMapping("/driving_instructor_comments_2")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_comments_2")) {
            model.addAttribute("newComment", new Comment());
            lessonList = lessonRepo.findAll();
            lessonForLearnerList = lessonRepo.findByIdLearnerOrderByDateAsc((Long) session.getAttribute("commentIdLearner"));
            lessonForLearnerAfterList = new ArrayList<>();
            for (Lesson lesson : lessonForLearnerList) {
                if (lesson.getDate().before(new Date()) && null == lesson.getComment() && lesson.getIdDrivingInstructor() == (Long) session.getAttribute("id") && lesson.getStatus().contains("done")) {
                    lessonForLearnerAfterList.add(lesson);
                }
            }
            model.addAttribute("lessonForLearnerAfterList", lessonForLearnerAfterList);
            error = "";
            news = "";
            return "driving_instructor_comments_2";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/save-comment")
    public String addComment(@ModelAttribute Comment comment, @RequestParam("commentContent") String commentContent, @RequestParam("grade1") int grade, @RequestParam(defaultValue = "", name = "lesson1") Long lesson) {
        if (false) {
            error = "This email already exists";
            return "redirect:/sign-up";
        }
        comment.setComment(commentContent);
        comment.setDate(new Date());
        comment.setGrade(grade);
        comment.setIdDrivingInstructor((Long) session.getAttribute("id"));
        comment.setLesson(lessonRepo.findByIdLesson(lesson));
        comment.setIdLearner((Long) session.getAttribute("commentIdLearner"));
        comment.setUserDetailInstructor(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("id")));
        comment.setUserDetailLearner(userDetailRepo.findByIdUserDetail((Long) session.getAttribute("commentIdLearner")));
        session.removeAttribute("commentIdLearner");
        commentRepo.save(comment);
        news = "Comment saved";
        return "redirect:/driving_instructor_comments";
    }

    @ModelAttribute("error")
    public String error() {
        return error;
    }

    @ModelAttribute("news")
    public String news() {
        return news;
    }

}
