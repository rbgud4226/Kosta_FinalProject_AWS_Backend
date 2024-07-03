package com.example.demo.oracledb.charts;

import com.example.demo.oracledb.users.Users;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/auth/chart")
public class ChartsController {
  @Autowired
  private ChartsService service;

  @PostMapping("/add")
  public String addChart(ChartsDto dto){
    ChartsDto cd = dto;
    if(cd.getEd().isEmpty()){
      cd.setEd(cd.getSt());
    }
    if(cd.getSt().compareTo(cd.getEd())>0){
      cd.setEd(cd.getSt());
    }
    service.save(cd);
    return "redirect:/index_emp";
  }

  @ResponseBody
  @PostMapping("/checkbox")
  @Transactional
  public void editChart(@RequestParam String taskid, @RequestParam String charstatus){
    ChartsDto cdto = service.get(Integer.parseInt(taskid));
    cdto.setChartStatus(charstatus);
    service.save(cdto);
  }

  @GetMapping("/gantt")
  public String ganttForm(){
    return "auth/charts/gantt";
  }

  @GetMapping("/calendar")
  public String calendarForm(){
    return "auth/charts/calendar";
  }

  @GetMapping("/data")
  @ResponseBody
  public ArrayList<ChartsDto> data(HttpSession session){
    String loginid = (String) session.getAttribute("loginId");
    ArrayList<ChartsDto> list = service.ganttList(loginid);
    return list;
  }

  @GetMapping("/cdata")
  @ResponseBody
  public ArrayList<Map> Cdata(){
    ArrayList<Map> list = new ArrayList<>();
    ArrayList<ChartsDto> l = service.getAll();
    for(ChartsDto c : l){
      Map map = new HashMap<>();
      map.put("title", c.getTitle());
      map.put("start", c.getSt());
      map.put("end", c.getEd());
      list.add(map);

    }
    return list;
  }

  @RequestMapping("/del")
  public String delbyid(int id){
    service.del(id);
    return "redirect:/index_emp";
  }

  @PostMapping("/share")
  public String ShareChart(@RequestParam(name = "userid", required = false) List<String> userids, @RequestParam(name="taskid")int taskid){
    ChartsDto dto = service.get(taskid);
    for (String userid : userids) {
      service.save(new ChartsDto(new Users(userid, null, null, null, 0, null), 0, dto.getChartResource(), dto.getTitle(),
          dto.getSt(), dto.getEd(), dto.getPercent(), dto.getDependencies(), dto.getChartStatus()));
    }
    return "redirect:/index_emp";
  }
}
