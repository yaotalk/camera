package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.mqtt.task.BatchRegistTask;
import com.minivision.camaraplat.mqtt.task.BatchTask;
import com.minivision.camaraplat.mqtt.task.BatchTaskContext;
import com.minivision.camaraplat.mvc.ex.ServiceException;
import com.minivision.camaraplat.service.FaceService;
import com.minivision.camaraplat.service.FaceSetService;
import com.minivision.camaraplat.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("face")
public class FaceController {

    @Autowired
    private FaceSetService faceSetService;
    @Autowired
    private FaceService faceService;

    @Autowired
    private BatchTaskContext taskContext;

    @GetMapping("/page")
    public ModelAndView pageinfo() {
        List<FaceSet> faceSets =  faceSetService.findByFaceplat();
        return new ModelAndView("faceset/faceadd").addObject("faceSets",faceSets);
    }

    @PostMapping
    public String addFace(Face face,
        @RequestParam("myfile") MultipartFile mfile,
        @RequestParam("facesetToken") String facesetToken) {
        try {
            String fileType = mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
            faceService.save(face, facesetToken, mfile.getBytes(), fileType);
            return "success";
        } catch (ServiceException | IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @DeleteMapping
    public String delFace(@RequestParam(name = "faceTokens", defaultValue = "") String faceTokens,
        @RequestParam(name = "facesetToken", defaultValue = "") String facesetToken) {
        faceService.delete(facesetToken, faceTokens);
        return "success";

    }

    @PatchMapping
    public String updateFace(Face face,@RequestParam(name = "facesetToken", defaultValue = "") String facesetToken) {
        faceService.update(face);
        return "success";
    }

    @GetMapping("/batch")
    public ModelAndView batchRegist() {
        BatchTask task = taskContext.getCurrentTask();
        ModelAndView mav = new ModelAndView("faceset/batch");
        if(task != null){
            mav.addObject("task",task);
        }
        return mav;
    }
    @GetMapping("/diskRoot")
    @ResponseBody
    public List<String> getDiskRoot() {
        return CommonUtils.getDiskRoot();
    }

    @GetMapping("/listDir")
    @ResponseBody
    public List<String> listDir(String parent) {
        return CommonUtils.listDir(parent);
    }

    @GetMapping("/getTask")
    @ResponseBody
    public BatchTask getTask(){
        return taskContext.getCurrentTask();
    }

    @PostMapping("/createTask")
    public ModelAndView createTask(String path, String faceSetToken , HttpServletRequest request, RedirectAttributes redirectAttributes){

        FaceSet faceSet = faceSetService.find(faceSetToken);

        BatchRegistTask
            task = taskContext.createBatchRegistTask(faceSet, request.getUserPrincipal().getName(), new File(path));

        try{
            taskContext.submitTask(task);
            redirectAttributes.addFlashAttribute("createTask", true);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("createTask", false);
        }
        return new ModelAndView("redirect:batch");
    }
}
