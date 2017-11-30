package com.chuxin.daoImp;

import com.chuxin.dao.VideoDao;
import com.chuxin.util.HttpMethodUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/12.
 *
 */
@Repository
public class VideoDaoImp implements VideoDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${wx.image}")
    private String imageUrl;

    @Override
    public List<Map<String, Object>> doFindGroupVideo(int startId,int sort) {
        String sql;
        if (sort == 0){
            sql = "select * from course where course.type ='weixin' limit ?,10 ";
        }else {
            sql = "select * from course where course.type ='weixin' and course.course_category_id= "+sort+" limit ?,10 ";
        }
        List<Map<String,Object>> videoList = jdbcTemplate.query(sql,new Object[]{(startId-1)*10},(ResultSet resultSet, int i) -> {
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("courseId",resultSet.getInt("id"));
                dataMap.put("courseName",resultSet.getString("book_name"));
                dataMap.put("describe",resultSet.getString("des"));
                dataMap.put("picId",resultSet.getString("pic_id"));
                return dataMap;
        });
        return videoList;
    }

    @Override
    public List<Map<String, Object>> doFindCourseVideoList(int courseId) {
        String sql = "select * from lessonperiod where lessonperiod.course_id = ?";
        List<Map<String,Object>> courseVideoList = jdbcTemplate.query(sql, new Object[]{courseId}, (ResultSet resultSet, int i) -> {

                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("name",resultSet.getString("name"));
                dataMap.put("node",resultSet.getInt("node"));
                dataMap.put("video_id",resultSet.getString("video_id"));
                return dataMap;

        });
        return courseVideoList;
    }

    @Override
    public List<Map<String,Object>> getDetailsByCourseId(int courseId) {
        String sql = "select * from course where course.id = ?";
        List<Map<String,Object>> courseVideoList = jdbcTemplate.query(sql, new Object[]{courseId},(ResultSet resultSet, int i) -> {
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("content",resultSet.getString("content"));

                dataMap.put("courseId",resultSet.getInt("id"));
                dataMap.put("courseName",resultSet.getString("book_name"));
                String url =  imageUrl + "/file/rest/download/links?ids="+ resultSet.getString("pic_id");
                String reustl = HttpMethodUtil.doGet(url);
                JSONObject jsonObject = JSONObject.fromObject(reustl);
                String picId = imageUrl + jsonObject.getJSONObject(resultSet.getString("pic_id")).get("href");
                dataMap.put("describe",resultSet.getString("des"));
                dataMap.put("picId",resultSet.getString("pic_id"));
                dataMap.put("pic_id",picId);
                return dataMap;

        });
        return courseVideoList;
    }

    @Override
    public List<Map<String, Object>> getVIPCourseList(int startId, int sort) {
        String sql;
        if (sort == 0){
            sql = "select * from coursegroup where coursegroup.type = 'Company 'and kind = 'weixin' limit ? , 10";
        }else {
            sql = "select * from coursegroup where coursegroup.type ='Company' and kind = 'weixin' and coursegroup.course_level_id= "+sort+" limit ?,10 ";
        }
        List<Map<String,Object>> videoList = jdbcTemplate.query(sql, new Object[]{(startId - 1) * 10}, (ResultSet resultSet, int i) -> {

                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("courseId",resultSet.getInt("id"));
                dataMap.put("courseName",resultSet.getString("name"));
                dataMap.put("describe",resultSet.getString("des"));
                dataMap.put("picId",resultSet.getString("pic_id"));
                dataMap.put("price",resultSet.getInt("price"));
                return dataMap;
        });
        return videoList;
    }

    @Override
    public List<Map<String, Object>> getVIPCourseDetailsByCourseGroupId(int courseGroupId) {
        String sql = "select  coupon_price,des,name,class_time,title,price,pic_id,lesson,id,c.bookname,content from coursegroup,(select group_concat(book_name) as bookname from coursegroupdetail,course where course_group_id = ? and course.id = coursegroupdetail.course_id " +
                ") as c where coursegroup.id = ? ";
        List<Map<String,Object>> vipCourseDetails = jdbcTemplate.query(sql, new Object[]{courseGroupId,courseGroupId}, (ResultSet resultSet, int i) -> {
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("courseName",resultSet.getString("name"));
                String url =  imageUrl + "/file/rest/download/links?ids="+ resultSet.getString("pic_id");
                String reustl = HttpMethodUtil.doGet(url);
                JSONObject jsonObject = JSONObject.fromObject(reustl);
                String picId = imageUrl + jsonObject.getJSONObject(resultSet.getString("pic_id")).get("href");
                dataMap.put("pic_id",picId);
                dataMap.put("price",resultSet.getInt("price"));
                dataMap.put("classTime",resultSet.getString("class_time"));
                dataMap.put("time",resultSet.getString("lesson"));
                dataMap.put("courseId",resultSet.getInt("id"));
                dataMap.put("content",resultSet.getString("content"));
                dataMap.put("bookname",resultSet.getString("bookname"));
                dataMap.put("picId",resultSet.getString("pic_id"));
                dataMap.put("describe",resultSet.getString("des"));
                dataMap.put("discount",resultSet.getString("coupon_price"));
                return dataMap;
        });
        return vipCourseDetails;
    }

}
