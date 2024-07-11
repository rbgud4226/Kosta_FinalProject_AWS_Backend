package com.example.demo.oracledb.workinoutrecords;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.oracledb.charts.ChartsDto;
import com.example.demo.oracledb.charts.ChartsService;
import com.example.demo.oracledb.depts.DeptsDto;
import com.example.demo.oracledb.depts.DeptsService;
import com.example.demo.oracledb.members.Members;
import com.example.demo.oracledb.members.MembersDto;
import com.example.demo.oracledb.members.MembersService;
import com.example.demo.oracledb.users.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*")
@Controller
public class WorkInOutRecordController {
  @Autowired
  private WorkInOutRecordService service;

  @Autowired
  private MembersService mservice;

  @Autowired
  private ChartsService cservice;
  
  @Autowired
  private DeptsService dservice;
  
  

  //개인
  //출퇴근 기록 페이지로 이동하기
  @ResponseBody
  @GetMapping("/auth/record/my")
  public Map myrecord() {
	Map map = new HashMap<>();
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String loginid =  auth.getName();
    System.out.println("loginid:" + loginid);
    MembersDto md = mservice.getByuserId(loginid);
    if (md == null) {
    	return null;
    }
    Members m = new Members(md.getUserid(), md.getMemberid(), md.getBirthdt(), md.getEmail(), md.getCpnum(), md.getAddress(), md.getMemberimgnm(), md.getHiredt(), md.getLeavedt(), md.getDeptid(), md.getJoblvid(), md.getMgrid(), null);
    //출근기록x
    boolean flag = false;
    boolean out = false;
    ArrayList<WorkInOutRecordDto> list = service.selectByDate(m.getMemberid());
    //출근기록O
    if (!list.isEmpty()) {
      flag = true;
      //오늘날짜 출근 등록번호(퇴근시 필요)
      map.put("num", list.get(0).getDaynum());
      if(list.get(0).getWorkOutTime() != null) {
    	  //퇴근,휴가 등 기록 된 컬럼
    	  out = true;
      }
    }
    //내 근무기록
    // 현재 달/년도 가져오기
    LocalDate currentDate = LocalDate.now();
    int currentMonth = currentDate.getMonthValue() - 1;
    int currentYear = currentDate.getYear();
    ArrayList<MemRecord> mylist = service.selectUser(currentMonth, currentYear, m.getMemberid());
    //이번달 출근 기록
    map.put("list", mylist);
    //오늘 날짜 출근 여부
    map.put("flag", flag);
    //사원번호 반환
    map.put("mynum", m.getMemberid());
    //퇴근 기록 반환
	map.put("out", out);
    return map;
  }

  //출근하기
  @ResponseBody
  @PostMapping("/record/in")
  public Map workin(String Members) {
	System.out.println(Members);
    MembersDto md = mservice.getByuserId(Members);
    Members m = new Members(md.getUserid(), md.getMemberid(), md.getBirthdt(), md.getEmail(), md.getCpnum(), md.getAddress(), md.getMemberimgnm(), md.getHiredt(), md.getLeavedt(), md.getDeptid(), md.getJoblvid(), md.getMgrid(), null);
    String type = "출근";

    //지각 체크
    LocalTime currentTime = LocalTime.now();
    LocalTime targetTime = LocalTime.of(9, 0);

    // 현재 시간이 9시 이전인지 확인
    if (currentTime.isAfter(targetTime)) {
      type = "지각";
    }
    WorkInOutRecordDto d = service.save(new WorkInOutRecordDto(0, m, null, null, null, null, null, type));

    Map map = new HashMap<>();
    map.put("num", d.getDaynum());
    map.put("flag", "true");
    map.put("state", type);
    return map;
  }


  //퇴근하기
  @ResponseBody
  @PostMapping("/out")
  public void workout(String Members, int memberid) {
	System.out.println("mem: "+Members+" /id:"+memberid);
    WorkInOutRecordDto w = service.select(memberid);
    String type = "정상근무";

    //근무 시간 확인
    LocalTime currentTime = LocalTime.now();
    LocalTime targetTime = LocalTime.of(18, 30);
    LocalTime targetTime2 = LocalTime.of(17, 50);
    //퇴근시간 기록
    w.setWorkOutTime(String.format("%02d:%02d", currentTime.getHour(), currentTime.getMinute()));
    if (currentTime.isAfter(targetTime)) {
      type = "추가근무";
    } else if (currentTime.isBefore(targetTime2)) {
      type = "조기퇴근";
    }
    if (w.getState().equals("지각")) {
      type = "지각";
    }
    //근무한 총 시간 확인
    LocalTime workinTime = LocalTime.parse(w.getWorkinTime());
    LocalTime workoutTime = LocalTime.parse(w.getWorkOutTime());
    Duration duration = Duration.between(workinTime, workoutTime);

    // 시간 차이
    long hours = duration.toHours();
    long minutes = duration.toMinutes() % 60;
    if (hours >= 9) {
      hours--;
    }
    String worktime = String.format("%02d:%02d", hours, minutes);
    w.setState(type);
    w.setWorkHours(worktime);
    service.save(w);
  }

  //휴가 기록
  @ResponseBody
  @PostMapping("/offday")
  public Map offRecord(String members, String res, String date1, String date2) {
    LocalDate startDate = LocalDate.parse(date1);
    LocalDate endDate = LocalDate.parse(date2);
    //멤버 정보
    MembersDto md = mservice.getByuserId(members);
    Members m = new Members(md.getUserid(), md.getMemberid(), md.getBirthdt(), md.getEmail(), md.getCpnum(), md.getAddress(), md.getMemberimgnm(), md.getHiredt(), md.getLeavedt(), md.getDeptid(), md.getJoblvid(), md.getMgrid(), null);
    String type = res;

    //주말 제외 저장하기
    LocalDate currentDate = startDate;
    while (!currentDate.isAfter(endDate)) {
      if (!isWeekend(currentDate)) {
        String dayOfWeek = currentDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        ;
        service.save(new WorkInOutRecordDto(0, m, dayOfWeek, currentDate, "00:00", "00:00", "00:00", type));
      }
      currentDate = currentDate.plusDays(1);
    }
    String chartTitle = members + " 의 " + res;
    cservice.save(new ChartsDto(new Users(members, null, null, null, 0, null),
        0, "휴가", chartTitle, date1, date2, 100, null, "no"));
    Map map = new HashMap<>();
    map.put("flag", "success");
    return map;
  }

  // 주말확인
  private static boolean isWeekend(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
  }

  //내 근태기록 확인하기
  @ResponseBody
  @GetMapping("/getmonth")
  public Map myrecord(String Members, int count) {
    MembersDto md = mservice.getByuserId(Members);
    if(md == null ) {
    	return null;
    }
    Members m = new Members(md.getUserid(), md.getMemberid(), md.getBirthdt(), md.getEmail(), md.getCpnum(), md.getAddress(), md.getMemberimgnm(), md.getHiredt(), md.getLeavedt(), md.getDeptid(), md.getJoblvid(), md.getMgrid(), null);
    // 현재 날짜 가져오기
    LocalDate currentDate = LocalDate.now();
    //필요 달/년도 가져오기
    int currentMonth = currentDate.getMonthValue();
    int currentYear = currentDate.getYear();
    // 이전 달로 이동
    currentMonth = currentMonth + count - 1;
    if (currentMonth == 0) {
      currentMonth = 12;
      currentYear--;
    }
    ArrayList<MemRecord> list = service.selectUser(currentMonth, currentYear, m.getMemberid());
    Map map = new HashMap<>();
    map.put("list", list);
    return map;
  }

  //부서 근태 부서장 페이지 이동
  @ResponseBody
  @GetMapping("/auth/record/dept")
  public Map deptList(int dept) {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	String loginid =  auth.getName();
	DeptsDto d = dservice.getByDeptId(dept);
	Map map = new HashMap<>();
	map.put("record",deptLeader(deptRecord(dept, 0)));
	map.put("list", deptRecord(dept, 0));
    return map;
  }

  //관리자(부서별 데이터 가져오기)
  @ResponseBody
  @GetMapping("/record/list")
  public ArrayList<ChartDeptMember> deptRecord(int dept, int cnt) {
    // 현재 날짜 가져오기
    LocalDate currentDate = LocalDate.now();
    // 현재 달/년도 가져오기
    int currentMonth = currentDate.getMonthValue();
    int currentYear = currentDate.getYear();

    // 이전 달로 이동
    int previousMonth = currentMonth + cnt -1;
    int previousYear = currentYear;
    if (previousMonth == 0) {
      previousMonth = 12;
      previousYear--;
    }
    ArrayList<ChartDeptMember> data = service.chartMonthandDept(previousMonth, previousYear, dept);
    return data;
  }
  
  //부서 통계용
  public Map deptLeader(ArrayList<ChartDeptMember> data) {
	 int overWork = 0;
	 int worktime = 0;
	 ArrayList<String> latemem = new ArrayList<String>();
	 ArrayList<String> danmem = new ArrayList<String>();
	 
     for(ChartDeptMember n : data) {
    	 //오버 워크 시간 합하기
    	 overWork += change(n.getOverWork());
    	 //근무시간 합하기
    	 worktime += change(n.getWorkTime());
    	 //지각자 통계내기
    	 if(n.getLateCount()>0) {
    		 latemem.add(n.getName());
    		 if(n.getLateCount()>4) {
    			 danmem.add(n.getName());
    		 }
    	 }
     }
     if(data.size()>0) {
         overWork /= data.size();
         worktime /= data.size();
     }

     int overavgHours = overWork / 60;
     int overavgMin = overWork % 60;

     int workavgHours = worktime / 60;
     int workavgMin = worktime % 60;
     
     // 시간 형식 변환
     String overAvgTime = String.format("%02d:%02d", overavgHours, overavgMin);
     String workAvgTime = String.format("%02d:%02d", workavgHours, workavgMin);
     
     Map map = new HashMap<>();
     map.put("overAvgTime", overAvgTime);
     map.put("workAvgTime", workAvgTime);
     map.put("latemem", latemem);
     map.put("danmem", danmem);
     
     return map;
  }
  
  // 시간 문자열을 분 단위로 변환
  public static int change(String time) {
      String[] parts = time.split(":");
      int hours = Integer.parseInt(parts[0]);
      int minutes = Integer.parseInt(parts[1]);
      return hours * 60 + minutes;
  }
  
  //관리자용 페이지(부서별 근무시간 통계 자료)
  @ResponseBody
  @GetMapping("/auth/record/admin")
  public Map admin(Model model) {
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  String loginid =  auth.getName();
      LocalDate currentDate = LocalDate.now();
      int currentYear = currentDate.getYear();
      
      ArrayList<DeptsDto> deptlist = dservice.getAll();
      
      Map<String, ArrayList<DeptsYearWorkData>> adminLineData = new HashMap<>();
      ObjectMapper mapper = new ObjectMapper();
      
      //부서별 데이터 삽입
      for (DeptsDto d : deptlist) {
          ArrayList<DeptsYearWorkData> deptList = service.deptYearData(currentYear, d.getDeptid());
          adminLineData.put(d.getDeptnm(), deptList);
      }
            
      return adminLineData;
  }
  
  @ResponseBody
  @GetMapping("/deptlist")
  public Map deptlist() {
	    Map map = new HashMap<>();
	    map.put("deptlist", dservice.getAll());
	    return map;
  }
  
  @ResponseBody
  @GetMapping("/over")
  public Map over() {
	  LocalDate currentDate = LocalDate.now();
	// 현재 달/년도 가져오기
	    int currentMonth = currentDate.getMonthValue() - 1;
	    int currentYear = currentDate.getYear();
	    
	    ArrayList<OverWorkData> list = service.overStatistics(currentYear, currentMonth);
	    
	    Map map = new HashMap<>();
	    map.put("overAvgTime", list);
	    return map;
  }
  
//  
//  @PostMapping("/member")
//  public void test(String members, String date1, String date2) {
//    LocalDate startDate = LocalDate.parse(date1);
//    LocalDate endDate = LocalDate.parse(date2);
//    //멤버 정보
//    MembersDto md = mservice.getByuserId(members);
//    Members m = new Members(md.getUserid(), md.getMemberid(), md.getBirthdt(), md.getEmail(), md.getCpnum(), md.getAddress(), md.getMemberimgnm(), md.getHiredt(), md.getLeavedt(), md.getDeptid(), md.getJoblvid(), md.getMgrid(), null);
//    
//    //주말 제외 저장하기
//    LocalDate currentDate = startDate;
//    while (!currentDate.isAfter(endDate)) {
//      if (!isWeekend(currentDate)) {
//        String dayOfWeek = currentDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
//        String type = "정상근무";
//        
//        // 출근 시간 범위 설정 (8:30 ~ 9:10)
//        LocalTime startTimeMin = LocalTime.of(8, 30);
//        LocalTime startTimeMax = LocalTime.of(9, 10);
//
//        // 퇴근 시간 범위 설정 (18:00 ~ 20:00)
//        LocalTime endTimeMin = LocalTime.of(17, 30);
//        LocalTime endTimeMax = LocalTime.of(21, 0);
//
//        // 랜덤한 출근 시간 생성 (8:30 이상, 9:10 미만)
//        LocalTime actualArrivalTime = generateRandomTime(startTimeMin, startTimeMax);
//
//        // 출근 시간 출력
//        System.out.println("출근 시간: " + actualArrivalTime);
//
//        // 출근 시간과 비교하여 지각 여부 판단
//        if (actualArrivalTime.isAfter(LocalTime.of(9, 0))) {
//        	type = "지각";
//        }
//
//        // 랜덤한 퇴근 시간 생성 (18:00 이상, 20:00 미만)
//        LocalTime actualDepartureTime = generateRandomTime(endTimeMin, endTimeMax);
//        
//        
//        // 퇴근 시간 출력
//        System.out.println("퇴근 시간: " + actualDepartureTime);
//        
//        LocalTime targetTime = LocalTime.of(18, 30);
//        LocalTime targetTime2 = LocalTime.of(17, 50);
//        if(type.equals("정상근무")) {
//            if (actualDepartureTime.isAfter(targetTime)) {
//                type = "추가근무";
//            } else if (actualDepartureTime.isBefore(targetTime2)) {
//                type = "조기퇴근";
//           }
//        }
//
//        // 출근 시간과 퇴근 시간의 차이 계산 및 출력
//
//        Duration duration = Duration.between(actualArrivalTime, actualDepartureTime);
//        
//        // 분 단위로 계산된 근무 시간을 HH:mm 형태로 변환
//        long minutesWorked = duration.toMinutes();
//        long hours = minutesWorked / 60;
//        if(hours>9) {
//        	hours--;
//        }
//        long minutes = minutesWorked % 60;
//        String formattedTime = String.format("%02d:%02d", hours, minutes);
//
//        // 실제 근무 시간 출력
//        System.out.println("실제 근무 시간: " + formattedTime);
//
//        // 결과 출력
//        System.out.println("출근 상태: " + type);
//        
//        
//        service.save(new WorkInOutRecordDto(0, m, dayOfWeek, currentDate, String.format("%02d:%02d",actualArrivalTime.getHour(), actualArrivalTime.getMinute()),
//        		String.format("%02d:%02d",actualDepartureTime.getHour(), actualDepartureTime.getMinute()), formattedTime, type));
//      }
//      currentDate = currentDate.plusDays(1);
//    }
//
//  }
////랜덤한 시간 생성 메서드
//  private static LocalTime generateRandomTime(LocalTime minTime, LocalTime maxTime) {
//	  
//	   // 시작 시간과 종료 시간 사이의 분 단위로 변환
//      long startMinutes = minTime.toSecondOfDay() / 60;
//      long endMinutes = maxTime.toSecondOfDay() / 60;
//
//      // 랜덤한 분 값 생성
//      long randomMinutes = ThreadLocalRandom.current().nextLong(startMinutes, endMinutes + 1);
//
//      // 생성된 분 값을 LocalTime으로 변환
//      LocalTime randomTime = minTime.plus(randomMinutes - startMinutes, ChronoUnit.MINUTES);
//      return randomTime;
//  }
//  
  
}
