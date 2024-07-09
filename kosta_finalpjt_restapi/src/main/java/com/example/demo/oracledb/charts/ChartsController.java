package com.example.demo.oracledb.charts;

import com.example.demo.oracledb.auth.MyTokenProvider;
import com.example.demo.oracledb.users.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/chart")
public class ChartsController {
  @Autowired
  private ChartsService service;

  @Autowired
  private MyTokenProvider myTokenProvider;

  @PostMapping("/add")
  public ResponseEntity<String> addChart(@RequestParam int taskid, @RequestParam String title, @RequestParam String chartResource, @RequestParam String st,
                                         @RequestParam String ed, @RequestParam int percent, @RequestParam String users){
    ChartsDto cd = new ChartsDto(new Users(users, null, null, null, 0, null),
        taskid, chartResource, title, st, ed, percent, null, "yes");
    if(cd.getEd().isEmpty()){
      cd.setEd(cd.getSt());
    }
    if(cd.getSt().compareTo(cd.getEd())>0){
      cd.setEd(cd.getSt());
    }
    service.save(cd);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/checkbox")
  @Transactional
  public ResponseEntity<String> editChart(@RequestParam String taskid, @RequestParam String charstatus){
    ChartsDto cdto = service.get(Integer.parseInt(taskid));
    cdto.setChartStatus(charstatus);
    service.save(cdto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/gantt")
  public ResponseEntity<String> ganttForm(){
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/calendar")
  public ResponseEntity<String> calendarForm(){
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/list")
  public ResponseEntity<List<ChartsDto>> list(HttpServletRequest req){
    String token = myTokenProvider.resolveToken(req);
    String id = myTokenProvider.getUserName(token);
    List<ChartsDto> list = service.getbyUsers(id);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/data")
  public ResponseEntity<List<ChartsDto>> data(HttpServletRequest req){
    String token = myTokenProvider.resolveToken(req);
    String id = myTokenProvider.getUserName(token);
    List<ChartsDto> list = service.ganttList(id);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/cdata")
  public ResponseEntity<List<Map<String, Object>>> Cdata(HttpServletRequest req){
    String token = myTokenProvider.resolveToken(req);
    String id = myTokenProvider.getUserName(token);
    List<Map<String, Object>> list = new ArrayList<>();
    List<ChartsDto> l = service.getbyUsers(id);
    for(ChartsDto c : l){
      Map map = new HashMap<>();
      map.put("title", c.getTitle());
      map.put("start", c.getSt());
      map.put("end", c.getEd());
      list.add(map);
    }
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @DeleteMapping("/del")
  public ResponseEntity<Void> delbyid(int id){
    service.del(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/share")
  public ResponseEntity<Void> ShareChart(@RequestParam(name = "userid", required = false) List<String> userids, @RequestParam(name="taskid")int taskid){
    ChartsDto dto = service.get(taskid);
    for (String userid : userids) {
      service.save(new ChartsDto(new Users(userid, null, null, null, 0, null), 0, dto.getChartResource(), dto.getTitle(),
          dto.getSt(), dto.getEd(), dto.getPercent(), dto.getDependencies(), dto.getChartStatus()));
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
