package com.example.demo.oracledb.charts;

import com.example.demo.oracledb.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChartsService {
  @Autowired
  private ChartsDao dao;

  public ChartsDto  save(ChartsDto dto) {
    ChartsDto cdto = dto;
    if(cdto.getChartStatus() == null){
      cdto.setChartStatus("yes");
    }
    Charts g = dao.save(new Charts(dto.getUsers(), dto.getTaskid(), dto.getChartResource(), dto.getTitle(), dto.getSt(),
        dto.getEd(), dto.getPercent(), dto.getDependencies(), cdto.getChartStatus()));
    return new ChartsDto(g.getUsers(), g.getTaskid(), g.getChartResource(), g.getTitle(), g.getSt(), g.getEd(),
        g.getPercent(), g.getDependencies(), g.getChartStatus());
  }

  public ChartsDto get(int id) {
    Optional<Charts> optional = dao.findById(id);
    Charts g = optional.get();
    return new ChartsDto(g.getUsers(), g.getTaskid(), g.getChartResource(), g.getTitle(), g.getSt(),
        g.getEd(), g.getPercent(), g.getDependencies(), g.getChartStatus());
  }

  public ArrayList<ChartsDto> getAll() {
    List<Charts> l = dao.findAll();
    ArrayList<ChartsDto> list = new ArrayList<>();
    for (Charts g : l) {
      list.add(new ChartsDto(g.getUsers(), g.getTaskid(), g.getChartResource(), g.getTitle(), g.getSt(),
          g.getEd(), g.getPercent(), g.getDependencies(), g.getChartStatus()));
    }
    return list;
  }

  public ArrayList<ChartsDto> getbyUsers(String id) {
    List<Charts> l = dao.findByUsersOrderByTaskidDesc(new Users(id, null, null, null, 0, null));
    int i=0;
    ArrayList<ChartsDto> list = new ArrayList<>();
    for (Charts g : l) {
      list.add(new ChartsDto(g.getUsers(), g.getTaskid(), g.getChartResource(), g.getTitle(), g.getSt(),
          g.getEd(), g.getPercent(), g.getDependencies(), g.getChartStatus()));
      i++;
    }
    for(int j=0; j<10-i;j++){
      list.add(new ChartsDto());
    }
    return list;
  }

  public ArrayList<ChartsDto> ganttList(String id) {
    List<Charts> l = dao.findByUsersOrderByTaskidDesc(new Users(id, null, null, null, 0, null));
    ArrayList<ChartsDto> fillteredList = l.stream()
    		.map(g -> new ChartsDto(g.getUsers(), g.getTaskid(), g.getChartResource(), g.getTitle(), g.getSt(), g.getEd(), g.getPercent(), g.getDependencies(), g.getChartStatus()))
        .filter(chartsDto -> chartsDto.getChartStatus().equals("yes"))
        .collect(Collectors.toCollection(ArrayList::new));
    return fillteredList;
  }

  public void del(int id) {
    dao.deleteById(id);
  }
}
