package com.example.application.controller.drivingInstructor;

import com.example.application.dao.*;
import com.example.application.dao.repo.CommentRepo;
import com.example.application.dao.repo.LessonRepo;
import com.example.application.dao.repo.UserRepo;
import com.example.application.utils.SessionUtil;
import com.sun.deploy.net.HttpResponse;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.net.www.http.HttpClient;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DrivingInstructorDocumentsController {

    private HttpSession session;
    private SessionUtil sessionUtil;
    private UserRepo userRepo;
    private String error;
    private String news;
    private List<String> detailsList;

    @Autowired
    public DrivingInstructorDocumentsController(HttpSession session, SessionUtil sessionUtil, UserRepo userRepo) {
        this.sessionUtil = sessionUtil;
        this.session = session;
        this.userRepo = userRepo;
    }

    @GetMapping("/driving_instructor_documents")
    public String get(Model model) throws IOException {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_documents")) {
            error = "";
            news = "";
            model.addAttribute("newDocument", "");
            detailsList = new ArrayList<>();

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("localhost");
            ftpClient.login("root", "");
            FTPFile[] files = ftpClient.listFiles();

            DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (FTPFile file : files) {
                String details = file.getName();
                detailsList.add(details);
            }
            ftpClient.logout();
            ftpClient.disconnect();
            model.addAttribute("details", detailsList);
            return "driving_instructor_documents";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/download-file")
    public String downloadFile(@RequestParam("detail") String detail) {
        if (sessionUtil.checkIfThisRoleCanEnterHere(session, "/driving_instructor_documents")) {
            return "redirect:/driving_instructor_documents";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/save-driving-instructors-documents")
    public String saveDrivingInstructorDocuments(@RequestParam("file") File file) throws IOException {

        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        try {
            client.connect("localhost");
            client.login("root", "");

            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            String filename = "C:\\Users\\HP\\Desktop\\" + file.getPath();
            fis = new FileInputStream(filename);
            client.storeFile(file.getPath(), fis);
            client.logout();
        } catch (IOException e) {
            System.out.println("FTP error!!!");
            e.printStackTrace();
        } finally {
            System.out.println("FTP finallyy!!!");

            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/driving_instructor_documents";
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
