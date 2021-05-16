package com.example.application.controller.drivingInstructor;

import com.example.application.dao.Comment;
import com.example.application.dao.Lesson;
import com.example.application.dao.User;
import com.example.application.dao.repo.CommentRepo;
import com.example.application.dao.repo.LessonRepo;
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
public class DrivingInstructorCommentsController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private LessonRepo lessonRepo;
    private List<User> userLearnerList;
    private List<User> userLearnerGoodList;
    private List<Lesson> lessonList;
    private List<Lesson> lessonForLearnerAfterList;
    private String error;
    private String news;
    private CommentRepo commentRepo;
    private List<Comment> commentList;

    @Autowired
    public DrivingInstructorCommentsController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo, LessonRepo lessonRepo, CommentRepo commentRepo) {
        this.sessionUtil = sessionUtil;
        this.session = session;
        this.userRepo = userRepo;
        this.lessonRepo = lessonRepo;
        this.commentRepo = commentRepo;
    }

    @GetMapping("/driving_instructor_comments")
    public String get(Model model) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_comments")) {
            error = "";
            news = "";
            commentList = commentRepo.findByIdDrivingInstructor((Long) session.getAttribute("id"));
            model.addAttribute("commentList", commentList);
            userLearnerList = userRepo.findByRole("learner");
            userLearnerGoodList = new ArrayList<>();
            for (User user : userLearnerList) {
                lessonList = lessonRepo.findByIdLearnerOrderByDateAsc(user.getIdUser());
                lessonForLearnerAfterList = new ArrayList<>();
                for (Lesson lesson : lessonList) {
                    if (lesson.getDate().before(new Date()) && null == lesson.getComment() && lesson.getIdDrivingInstructor() == (Long) session.getAttribute("id") && lesson.getStatus().contains("done")) {
                        lessonForLearnerAfterList.add(lesson);
                    }
                }
                if (lessonForLearnerAfterList.size() != 0)
                    userLearnerGoodList.add(user);
            }
            model.addAttribute("userLearnerGoodList", userLearnerGoodList);
            model.addAttribute("newComment", new Comment());
            return "driving_instructor_comments";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/continue-comment")
    public String continueComment(@ModelAttribute Comment comment, @RequestParam(defaultValue = "0", name = "learner1") Long learner) {
        if (userLearnerGoodList.size() == 0) {
            news = "You do not have any lessons to comment";
            return "redirect:/driving_instructor_comments";
        }
        comment.setIdLearner(learner);
        session.setAttribute("commentIdLearner", learner);
        return "redirect:/driving_instructor_comments_2";
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
