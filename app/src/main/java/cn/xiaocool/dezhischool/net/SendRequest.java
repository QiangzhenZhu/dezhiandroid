package cn.xiaocool.dezhischool.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xiaocool.dezhischool.dao.CommunalInterfaces;
import cn.xiaocool.dezhischool.utils.LogUtils;
import cn.xiaocool.dezhischool.utils.SPUtils;

/**
 * Created by 潘 on 2016/3/31.
 */
public class SendRequest {
    private Context mContext;
    private Handler handler;


    public SendRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;


    }
    //获取老师点评
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetTeacherComment&studentid=661&begintime=0&endtime=1469863987
    public void getTeacherComment(final String childId,final String begintime, final String endtime) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&begintime=" + begintime + "&endtime=" + endtime;
                Log.e("teacherComment-----", WebHome.GET_TEACOMMENT + data);
                String result_data = NetUtil.getResponse(WebHome.GET_TEACOMMENT, data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETTEACOMMENT;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    /**
     * 获取园丁列表
     */
    public void getTeacher(final String schoolid) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + schoolid;
                //String result_data = NetUtil.getResponse(WebHome.TEACHER, data);
                String result_data = NetUtil.getResponse(WebHome.Meeting_Teacher, data);
                LogUtils.e("NewsRequest-teacher", WebHome.TEACHER + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TEACHER;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 会议列表
     * @param
     */
    public void getMeetingTeacher(final Context context, final String schoolid, final String keyword) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + schoolid+"&userid="+SPUtils.get(context,LocalConstant.USER_ID,"")+"&keyword="+keyword;

                //String result_data = NetUtil.getResponse(WebHome.TEACHER, data);
                String result_data = NetUtil.getResponse(NetConstantUrl.GET_MEETING_LIST, data);
                LogUtils.e("NewsRequest-teacher", WebHome.TEACHER + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.MEETING_TEACHER;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //在线请假的学生列表
    public void getLeave_stu(final String userid) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid;
                String result_data = NetUtil.getResponse(WebHome.LEAVE_STU, data);
                Log.e("reason", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.LEAVE_STU;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //班级和学生列表
    public void myclass_stu(final String userid) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&teacherid=" + userid;
                String result_data = NetUtil.getResponse(WebHome.CLASS_STULIST, data);
                Log.i("CLASS_STULIST",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CLASS_STU;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //班级和学生列表
    public void meeting_teacher(final Context context) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + SPUtils.get(context,LocalConstant.SCHOOL_ID,"");
                String result_data = NetUtil.getResponse(NetConstantUrl.GET_CLASS_TEACHER_LIST, data);
                Log.i("CLASS_STULIST",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.MEETING_TEACHER;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 发送信息群发
     * 接口名称用户发送消息(post传输) <>
     * 接口地址：a=send_message <>
     * 入参：send_user_id,schoolid,send_user_name,message_content,receiver_user_id,picture_url <>
     * 出参：无 <>
     * Demo:http://wxt.xiaocool.net/index.php?g=apps&m=message&a=send_message&send_user_id=600&schoolid=1&send_user_name=呵呵
     * &message_content=222&receiver_user_id=597,600&picture_url=ajhsdiaho.png<>
     */
    public void send_newsgroup(final String send_user_id,final String schoolid,final String message_content,final String sendName, final String receiver_user_id, final String picture_url, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "";
                if (picture_url.equals("null")) {
                    data = "&send_user_id=" + send_user_id + "&schoolid=" + schoolid + "&send_user_name=" + sendName
                            + "&message_content=" + message_content + "&receiver_user_id=" + receiver_user_id;
                } else {
                    data = "&send_user_id=" + send_user_id + "&schoolid=" + schoolid + "&send_user_name=" + sendName
                            + "&message_content=" + message_content + "&receiver_user_id=" + receiver_user_id + "&picture_url=" + picture_url;
                }
                Log.e("send_school_news",NetConstantUrl.SEND_SCHOOL_NEWS + data);
                String result_data = NetUtil.getResponse(NetConstantUrl.SEND_SCHOOL_NEWS, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    /*
    * 学生家长获取作业*/
    public void studentgethomework(final String userid,final String classid) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&type=1&receiverid=" + userid + "&classid=" + classid;
                String result_data = NetUtil.getResponse(NetConstantUrl.GET_CLASS_NEWS_RECEIVE, data);
//                LogUtils.e("NewsRequest-homework", WebHome.HOMEWORK + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SPACE_MYHOMEWORK;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    /**
     * 获取家庭作业列表
     */
    public void homework(final String userid,final String classid) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid + "&classid=" + classid;
                String result_data = NetUtil.getResponse(WebHome.HOMEWORK, data);
                LogUtils.e("NewsRequest-homework", WebHome.HOMEWORK + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.HOMEWORK;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public void send_homework(final String schoolid, final String userid,final String title, final String content, final String reciveid, final String picname, final String subject, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String data = "&type=1&teacherid=" +userid +"&schoolid="+schoolid +"&title=" + title + "&content=" + content + "&receiverid=" + reciveid + "&picture_url="+ picname + "&subject=" + subject;//+ picname
                String result_data = NetUtil.getResponse(WebHome.SEND_HOMEWORK, data);
//                LogUtils.e("send_homework",WebHome.SEND_HOMEWORK+data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //添加在线请假
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=addleave&teacherid=605&parentid=681&studentid=661&begintime=1469170284&endtime=1469170284&reason=&picture_url=newsgroup2021469263354727.jpg,newsgroup2021469263354727.jpg
    public void add_leave(final String schoolid,final String studentid,final String parentid,final String teacherid,final String classid,final String begintime, final String endtime, final String reason, final String leaveType) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&begintime=" + begintime + "&endtime=" + endtime + "&parentid=" + parentid +
                        "&reason=" + reason + "&studentid=" + studentid + "&teacherid=" + teacherid  + "&leavetype=" + leaveType+"&classid"+classid+"&schoolid"+schoolid;
                String result_data = NetUtil.getResponse(WebHome.LEAVE_ADD, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.LEAVE_ADD;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //发送老师点评
    public void send_teacherRemark(final String userid,final String studentId,final String content, final String learn,final String work, final String sing,final String labour, final String strain,final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&teacherid="+userid
                        +"&studentid="+studentId+"&content="+content+"&learn="+learn+"&work="+
                        work+"&sing="+sing+"&labour="+labour+"&strain="+strain;
                String result_data = NetUtil.getResponse(WebHome.ADD_TEACHERCOMMENT, data);
                LogUtils.e("send_replayleave", WebHome.ADD_TEACHERCOMMENT + data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=WriteMicroblog&userid=681&schoolid=1&type=1&content=8-4成长日记&classid=1&picurl=newsgroup6241470274805597.jpg,newsgroup7011470274805547.jpg

    /**
     * 发布动态
     * @param userid
     * @param schoolid
     * @param content
     * @param classid
     * @param picurl
     * @param KEY
     */
    public void send_trend(final String userid, final String schoolid, final String classid, final String content, final String picurl, final String videoName, final String videoImgName,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "";
                if (picurl.equals("null")) {
                    data = "&userid=" + userid + "&schoolid=" + schoolid + "&type=" + "1"
                            + "&content=" + content + "&classid=" + classid + "&video_phone=" + videoImgName + "&video=" + videoName;
                } else {
                    data = "&userid=" + userid + "&schoolid=" + schoolid + "&type=" + "1"
                            + "&content=" + content + "&classid=" + classid + "&picurl=" + picurl + "&video_phone=" + videoImgName + "&video=" + videoName;
                }
                String result_data = NetUtil.getResponse(NetConstantUrl.SEND_TREND, data);
                Log.e("send_trend-----",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //http://hyx.xiaocool.net/index.php?g=apps&m=school&a=publishnotice&userid=599&title=%E6%A0%87%E9%A2%98&content=%E5%86%85%E5%AE%B9&photo=hyx8221472460032673.jpg,hyx9381472460032660.jpg&reciveid=604
    /**
     * 发布校内通知
     * @param userid
     * @param title
     * @param content
     * @param photo
     * @param reciveid
     * @param KEY
     */
    public void send_school_announce(final String userid, final String title, final String content, final String photo, final String reciveid, final String schoolid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "";
                if (photo.equals("null")) {
                    data = "&userid=" + userid + "&title=" + title + "&content=" + content
                            + "&reciveid=" + reciveid +"&schoolid=" + schoolid;
                } else {
                    data = "&userid=" + userid + "&title=" + title + "&content=" + content
                            + "&reciveid=" + reciveid + "&photo=" + photo + "&schoolid=" + schoolid;
                }
                String result_data = NetUtil.getResponse(NetConstantUrl.SEND_ANNOUNCEMENT, data);
                Log.e("send_trend-----",NetConstantUrl.SEND_ANNOUNCEMENT+data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://hyx.xiaocool.net/index.php?g=apps&m=teacher&a=addhomework&schoolid=1&teacherid=599&title=%E5%91%A8%E5%9B%9B%E4%BD%9C%E4%B8%9A&content=%E4%BD%9C%E4%B8%9A%E5%86%85%E5%AE%B9%EF%BC%8C%E5%BF%AB%E6%9D%A5%E7%9C%8B&receiverid=654&picture_url=hyx8221472460032673.jpg,hyx9381472460032660.jpg
    public void send_class_new(final String schoolid, final String teacherid, final String title, final String content, final String receiverid,final String picture_url,final int type,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "";
                if (picture_url.equals("null")) {
                    data = "&type="+type+"&schoolid=" + schoolid + "&teacherid=" + teacherid + "&title=" + title
                            + "&content=" + content + "&receiverid=" + receiverid ;
                } else {
                    data = "&type="+type+"&schoolid=" + schoolid + "&teacherid=" + teacherid + "&title=" + title
                            + "&content=" + content + "&receiverid=" + receiverid + "&picture_url=" + picture_url;
                }
                String result_data = NetUtil.getResponse(NetConstantUrl.SEND_CLASS_NEW, data);
                Log.e("classnews",NetConstantUrl.SEND_CLASS_NEW+data);
                Log.e("send_class_new-----",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 修改性别
     */
    public void updateSex(final String userid,final int sex, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid + "&sex=" + sex;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=index&a=UpdateUserSex", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 更新名字
     */
    public void updateName(final String userid,final String name, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid + "&nicename=" + name;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=index&a=UpdateUserName", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 更新头像
     */
    public void updateHeadImg(final String userid,final String avatar, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid + "&avatar=" + avatar;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=index&a=UpdateUserAvatar", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void send_remark(final String id,final String userid, final String content, final String type,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid + "&id=" + id + "&content=" + content + "&type=" + type;
                Log.d("final String id", data);
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=school&a=SetComment", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }


        }.start();
    }

    /**
     * 点赞
     * @param workBindId
     * @param workPraiseKey
     */
    public void Praise(final String userId,final String workBindId, final int workPraiseKey) {
        LogUtils.d("weixiaotong", "getCircleList");
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "&userid=" + userId + "&id=" + workBindId + "&type=" + 1;
                    String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=school&a=SetLike", data);
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = workPraiseKey;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 取消点赞
     * @param id
     * @param KEY
     */
    public void DelPraise(final String userid,final String id, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                try {
                    String data = "userid=" + userid + "&id=" + id + "&type=" + "1";
                    String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=school&a=ResetLike", data);

                    LogUtils.e("getIndexSlideNewsList", result_data.toString());
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://hyx.xiaocool.net/index.php?g=apps&m=student&a=AddParentMessage&classid=1&schoolid=1&userid=674&content=%E5%86%85%E5%AE%B9

    /**
     * 发送反馈
     * @param userid
     * @param classid
     * @param schoolid
     * @param content
     * @param KEY
     */
    public void send_feedback(final String userid, final String classid, final String schoolid, final String content,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&classid=" + classid + "&schoolid=" + schoolid + "&userid=" + userid
                            + "&content=" + content ;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=student&a=AddParentMessage", data);
                Log.e("send_feedback-----",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://hyx.xiaocool.net/index.php?g=apps&m=student&a=FeedParentMessage&messageid=1&teacherid=599&feedback=%E5%9B%9E%E5%A4%8D%E5%86%85%E5%AE%B9
    public void feedback(final String messageid, final String teacherid, final String feedback,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&messageid=" + messageid + "&teacherid=" + teacherid + "&feedback=" + feedback;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=student&a=FeedParentMessage", data);
                Log.e("feedback-----",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://hyx.xiaocool.net/index.php?g=apps&m=index&a=SendMessageInfo&phone=18553546580,13276386385,15653579769&message=%E6%B5%8B%E8%AF%95%E4%B8%80%E4%B8%8B&userid=597
    public void sendGroupMessage(final String phone, final String userid, final String schoolid,final String message,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&phone=" + phone + "&userid=" + userid +"&schoolid="+schoolid+ "&message=" + message;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=index&a=SendMessageInfo", data);
                Log.e("sendGroup-----",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //http://hyx.xiaocool.net/index.php?g=apps&m=index&a=ServiceOnline&userid=597&schoolid=1&content=%E7%95%99%E8%A8%80
    public void sendFeedback(final String userid, final String schoolid, final String content,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid + "&schoolid=" + schoolid + "&content=" + content;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=index&a=ServiceOnline", data);
                Log.e("sendGroup-----",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //最新消息
    public void send_replayleave(final String leaveid,final String status, final String feedback,final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&leaveid=" + leaveid+"&teacherid="+ SPUtils.get(mContext,LocalConstant.USER_ID,"")+"&feedback="+feedback+"&status="+status;
                String result_data = NetUtil.getResponse(NetConstantUrl.NETBASEURL+"index.php?g=apps&m=teacher&a=replyleave", data);
                LogUtils.e("send_replayleave", "http://dezhi.xiaocool.net/index.php?g=apps&m=teacher&a=replyleave" + data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public void addressParentNew(final String stuId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + stuId;
                String result_data = NetUtil.getResponse(WebHome.MESSAGEADDRESS,
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.MESSAGEADDRESS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
