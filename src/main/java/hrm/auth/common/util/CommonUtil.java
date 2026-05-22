package hrm.auth.common.util;


import hrm.auth.common.dto.MsgResponse;
import hrm.auth.common.dto.SearchParamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.regex.Pattern;

@Component
public  class CommonUtil {

    public static List<String> bulkStrToList(String bulkStr){
        List<String> list = new ArrayList<>();
        if(bulkStr==null || bulkStr.trim().isEmpty()){
            return list;
        }
        String [] arr = bulkStr.split(",");
        list = Arrays.asList(arr);
        return list;
    }
    public static final List<String> openUrlsLike = Arrays.asList(
            "/auth/getToken",
            "/swagger-ui",
            "/api-docs",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars",
            "/configuration/ui",
            "/configuration/security"
            );
/*
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/api-docs/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/webjars/**"
 */


    public static List<String> prdctTypes = Arrays.asList("BARCODED_PRODUCT","NORMAL_PRODUCT");

    public static String removeHeadTailSpace(String input){
        if(input==null || input.isBlank() || input.equalsIgnoreCase("undefined")){
            return null;
        }
        input = input.trim();
        return input;
    }

    public static String removeAllSpace(String input){
        if(input==null || input.isBlank()){
            return null;
        }
        input = input.replaceAll("\\s+", "");  //this also remove head and tails space from string
        return input;
    }

    public static boolean isLastChar(String input,char ch){
        if(input==null || input.isBlank()){
            return false;
        }
        char lastCharacter = input.charAt(input.length()-1);
        if(lastCharacter==ch){
            return true;
        }
        return false;
    }


    public static String replaceRepeatedChar(String input, char ch) {
        if(input==null || input.isBlank()){
            return null;
        }
        String regex = Pattern.quote(Character.toString(ch)) + "+";
        return input.replaceAll(regex, Character.toString(ch));
    }


    public static List<Long> strListToLong(List<String> list){
        List<Long> longLst = new ArrayList<>();
        for(String str : list){
            longLst.add(Long.parseLong(str));
        }
        return longLst;
    }

    public static List<Long> reverseOrderList(List<Long> list){
        List<Long> result = new ArrayList<>();
        for(int i=list.size()-1 ; i>=0 ; i--){
            result.add(list.get(i));
        }
        return result;
    }



    public static Pageable getPageable(SearchParamDTO dto){
        Sort sort = dto.sortDir.equals("asc")?Sort.by(dto.sortField).ascending():Sort.by(dto.sortField).descending();
        Pageable pageable = PageRequest.of(dto.pageNum-1,dto.pageSize,sort);
        return pageable;

    }

    public static String replaceWord(String str, String repWord,String byWord){
        String finalStr = str.replace(repWord,byWord);
        return finalStr;
    }



    public static MsgResponse responseFromPage(Page<Map<String,Object>> page){
        MsgResponse response = new MsgResponse();
        response.setMessage("data retrived");
        response.setSuccess(true);
        Map<String,Object> mp = new HashMap<>();
        mp.put("listData",page.getContent());
        mp.put("totalPages",page.getTotalPages());
        mp.put("totalItems",page.getTotalElements());
        mp.put("pageNum",page.getNumber());
        mp.put("pageSize",page.getSize());
        response.setData(mp);
        return response;
    }


    public static MsgResponse responseFromObjectPage(Page<Object> page){
        MsgResponse response = new MsgResponse();
        response.setMessage("data retrived");
        response.setSuccess(true);
        Map<String,Object> mp = new HashMap<>();
        mp.put("listData",page.getContent());
        mp.put("totalPages",page.getTotalPages());
        mp.put("totalItems",page.getTotalElements());
        mp.put("pageNum",page.getNumber());
        mp.put("pageSize",page.getSize());
        response.setData(mp);
        return response;
    }

    public static String removeCharFromString(String input , char ch){
        String result = input.chars()
                .filter(c -> c != ch)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return result;
    }

    public static String removeWordFromString(String stringSeq , String wordToRemove){
        if(wordToRemove==null){
            return stringSeq;
        }
        String updatedString = stringSeq.replace(wordToRemove, "").trim();
        return updatedString;
    }

    public static String removeLastCharacter(String str){
        if(str==null){
            return str;
        }
        String result = str.substring(0, str.length() - 1);
        return  result;
    }

    public static String removeFirstChar(String str) {

        String result =  (str == null || str.length() < 2) ? str : str.substring(1);
        return  result;
    }

//   public static String currentUser(){
//        return  SecurityContextHolder.getContext().getAuthentication().getName();
//    }


    public static boolean isUrlOpenToAll(String requestUrl){
          for(String url : CommonUtil.openUrlsLike){
             if(requestUrl.contains(url)){
                 return true;
             }
          }
          return false;
    }

}

