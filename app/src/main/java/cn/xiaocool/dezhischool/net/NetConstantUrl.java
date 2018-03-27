package cn.xiaocool.dezhischool.net;

/**
 * Created by Administrator on 2016/8/24.
 */
public class NetConstantUrl {

    /**
     * 域名
     */
    public static final String NETBASEURL = "http://dezhi.xiaocool.net/";
    /**
     * 动态圈日志图片地址
     */
    public final static String NET_CIRCLEPIC_HOST = "http://dezhi.xiaocool.net/uploads/microblog/";

    /**
     * 五项公开专栏
     */
    public static final String FIVE_PUBLIC_URL = NETBASEURL + "index.php?g=portal&m=article";
    /**
     * 获取验证码
     * &phone
     */
    public static final String GET_PHONE_CODE = NETBASEURL + "index.php?g=apps&m=index&a=SendMobileCode";

    /**
     * 判断验证码是否正确
     * &phone
     * &code
     */
    /**
     *API接口地址
     */
    /**
     * API接口地址
     */
    public final static String NET_API_HOST = NETBASEURL + "index.php?g=apps&m=index&";
    public final static String NET_API_HOST1 = NETBASEURL + "index.php?g=apps&m=teacher&";


    public final static String NET_API_HOST2 = NETBASEURL + "index.php?g=apps&m=school&";
    public final static String NET_API_HOST3 = NETBASEURL + "index.php?g=apps&m=student&";
    public final static String NET_API_HOST4 = NETBASEURL + "index.php?g=Apps&m=Message&";
    public static final String PHONE_CODE_ISOK = NETBASEURL + "index.php?g=apps&m=index&a=UserVerify";
    //教师端地址
    public final static String NET_TEACHER_HOST = NETBASEURL + "index.php?g=apps&m=teacher&";
    //学校地址
    public final static String NET_SCHOOL_HOST = NETBASEURL + "index.php?g=apps&m=school&";

    /**
     * 忘记密码--设置密码
     * phone
     * pass
     */
    public static final String FORGET_SET_PSW = NETBASEURL + "index.php?g=apps&m=index&a=UpadataPass_Activate";
    /**
     * 修改密码
     * userid
     * pass
     */
    public static final String RESET_PSW = NETBASEURL + "index.php?g=apps&m=index&a=forgetPass_Activate";
    /**
     * 登录
     * 参数&phone=17862816935&password=123&type=1
     * type  1--老师  2--家长
     */
    public static final String LOGIN_URL = NETBASEURL + "index.php?g=apps&m=index&a=applogin";
    /**
     * 图片拼接地址
     */
    public static final String IMAGE_URL = NETBASEURL + "data/upload/";
    /**
     * 上传图片地址
     */
    public static final String PUSH_IMAGE = NETBASEURL + "index.php?g=apps&m=index&a=WriteMicroblog_upload";
    public static final String PUSH_VIDEO = NETBASEURL + "index.php?g=apps&m=index&a=video_upload";
    /**
     * 发布学校信息
     */
    public static final String SEND_SCHOOL_NEWS = NETBASEURL + "index.php?g=apps&m=message&a=send_message";
    /**
     * 发布动态
     * http://hyx.xiaocool.net/index.php?g=apps&m=index&a=WriteMicroblog_else&type=1
     * &userid=9188&content=%E6%A0%A1%E9%85%B7&schoolid=8&classid=91,92&picurl=1.jpg,2.jpg
     */
    public static final String SEND_TREND = NETBASEURL + "index.php?g=apps&m=index&a=WriteMicroblog_else";
    /**
     * 发布校内通知
     */
    public static final String SEND_ANNOUNCEMENT = NETBASEURL + "index.php?g=apps&m=school&a=publishnotice";
    /**
     * 获取老师职务
     */
    public static final String GET_DUTY = NETBASEURL + "index.php?g=apps&m=teacher&a=GetTeacherDuty";
    public static final String GET_TEACHERREVIEW = NETBASEURL + "index.php?g=apps&m=teacher&a=GetTeacherComment";
    /**
     * 获取老师对应的班级信息
     */
    public static final String GET_CLASSINFO = NETBASEURL + "index.php?g=apps&m=teacher&a=GetLeaderClass";
    /**
     * 获取校长所发送的学校信息
     */
    public static final String GET_SCHOOL_NEWS_SEND = NETBASEURL + "index.php?g=Apps&m=Message&a=user_send_message";
    /**
     * 获取家长所接收的学校信息
     */
    public static final String GET_SCHOOL_NEWS_RECEIVE = NETBASEURL + "index.php?g=Apps&m=Message&a=user_reception_message";

    /**
     * 获取家长所关联的学生
     */
    public static final String GET_USER_RELATION = NETBASEURL + "index.php?g=apps&m=index&a=GetUserRelation";

    /**
     * 获取全校班级
     */
    public static final String GET_SCHOOL_CLASS = NETBASEURL + "index.php?g=apps&m=school&a=getclasslist&schoolid=";

    /**
     * 根据班级id获取班级学生
     */
    public static final String GET_CLASS_BYID = NETBASEURL + "index.php?g=apps&m=school&a=getStudentlistByClassid";

    /**
     * 获取全校科室及科室下的老师
     */
    public static final String GET_SCHOOL_TEACHER = NETBASEURL + "index.php?g=apps&m=school&a=getteacherinfo&schoolid=";

    /**
     * 获取校长发送的校内通知
     */
    public static final String GET_ANNOUNCE_SEND = NETBASEURL + "index.php?g=apps&m=school&a=getnoticelist";

    /**
     * 获取老师接收的校内通知
     */
    public static final String GET_ANNOUNCE_RECEIVE = NETBASEURL + "index.php?g=apps&m=school&a=get_receive_notice";
    /**
     * 发送班级消息
     */
    public static final String SEND_CLASS_NEW = NETBASEURL + "index.php?g=apps&m=teacher&a=addhomework";
    /**
     * 获取班主任发布的班级消息
     */
    public static final String GET_CLASS_NEWS_SEND = NETBASEURL + "index.php?g=apps&m=teacher&a=gethomeworklist";

    /**
     * 获取家长接收的班级消息
     */
    public static final String GET_CLASS_NEWS_RECEIVE = NETBASEURL + "index.php?g=apps&m=student&a=gethomeworkmessage";

    /**
     * 校长获取全校班级消息
     */
    public static final String GET_CLASS_NEWS_ALL = NETBASEURL + "index.php?g=apps&m=teacher&a=GetAllClassInfo&schoolid=";

    /**
     * 校网学校概况获取列表
     */
    public static final String GET_WEB_SCHOOL_INTROUCE = NETBASEURL + "index.php?g=apps&m=school&a=getWebSchoolInfos&schoolid=";

    /**
     * 校网教师风采获取列表
     */
    public static final String GET_WEB_SCHOOL_TEACHER = NETBASEURL + "index.php?g=apps&m=school&a=getteacherinfos&schoolid=";
    /**
     * 校网学生秀场获取列表
     */
    public static final String GET_WEB_SCHOOL_STUDENT = NETBASEURL + "index.php?g=apps&m=school&a=getbabyinfos&schoolid=";
    /**
     * 校网精彩活动获取列表
     */
    public static final String GET_WEB_SCHOOL_ACTIVITY = NETBASEURL + "index.php?g=apps&m=school&a=getInterestclass&schoolid=";
    /**
     * 校网校园公告获取列表
     */
    public static final String GET_WEB_SCHOOL_NOTICE = NETBASEURL + "index.php?g=apps&m=school&a=getSchoolNotices&schoolid=";
    /**
     * 校网新闻动态获取列表
     */
    public static final String GET_WEB_SCHOOL_NEWS = NETBASEURL + "index.php?g=apps&m=school&a=getSchoolNews&schoolid=";

    /**
     * 校网系统消息
     */
    public static final String GET_WEB_SYSTEM_NEWS = NETBASEURL + "index.php?g=apps&m=index&a=getSystemMessages&term_id=3";
    /**
     * 使用帮助
     */
    public static final String GET_WEB_SYSTEM_HELP = NETBASEURL + "index.php?g=portal&m=article&a=system&id=16";
    /**
     * 关于我们
     */
    public static final String GET_WEB_SYSTEM_ABOUT = NETBASEURL + "index.php?g=portal&m=article&a=system&id=17";

    /**
     * 校网H5拼接地址
     */
    public final static String WEB_LINK = NETBASEURL + "index.php?g=portal&m=article";
    /**
     * 校网图片拼接地址
     */
    public final static String WEB_IMAGE_URL = "http://dezhi.xiaocool.net";
    /**
     * 系统消息拼接地址
     */
    public final static String SYSTEM_H5 = NETBASEURL + "index.php?g=portal&m=article&a=system&id=";

    /**
     * 获取个人信息
     */
    public static final String GET_USER_INFO = NETBASEURL + "index.php?g=apps&m=index&a=GetUsers";

    /**
     * 获取全校班级和老师
     */
    public static final String GET_CLASS_TEACHER = NETBASEURL + "index.php?g=apps&m=school&a=getclassteacherlist&schoolid=";

    /**
     * 获取全校班级和家长
     */
    public static final String GET_PARENT_ALL = NETBASEURL + "index.php?g=apps&m=school&a=GetSchoolParent&schoolid=";

    /**
     * 获取老师任教班级和家长
     */
    public static final String GET_PARENT_BYTEACHERID = NETBASEURL + "index.php?g=apps&m=school&a=GetTeacherClassStudentParent";

    /**
     * 获取对应班级id的班级和家长
     */
    public static final String GET_PARENT_BYCLASSID = NETBASEURL + "index.php?g=apps&m=school&a=GetSchoolParent";

    /**
     * 获取动态（家长）
     */
    public static final String GET_TRENDS_PARENT = NETBASEURL + "index.php?g=apps&m=index&a=GetMicroblog&type=1&schoolid=";

    /**
     * 获取总积分
     */
    public static final String GET_INTEGRATION_TOTAL = NETBASEURL + "/index.php?g=apps&m=index&a=GetIntegralList&userid=";

    /**
     * 获取积分列表
     */
    public static final String GET_INTEGRATION_LIST = NETBASEURL + "/index.php?g=apps&m=index&a=GetIntegralInfo&userid=";

    /**
     * 获取家长信箱（家长）
     */
    public static final String GET_FEEDBACK_PARENT = NETBASEURL + "index.php?g=apps&m=student&a=ParentGetParentMessage&userid=";

    /**
     * 获取家长信箱（老师有回复的）
     */
    public static final String GET_FEEDBACK_BE = NETBASEURL + "index.php?g=apps&m=student&a=GetParentMessageBe&schoolid=";
    /**
     * 获取家长信箱（老师无回复的）
     */
    public static final String GET_FEEDBACK_NULL = NETBASEURL + "index.php?g=apps&m=student&a=GetParentMessageNull&schoolid=";

    /**
     * 获取教师群发的短信（班主任）
     */
    public static final String GET_SHORT_MESSAGE = NETBASEURL + "index.php?g=apps&m=index&a=GetMobileMessage&userid=";

    /**
     * 五公开专栏（作业公告）
     */
    public static final String FIVE_PUBLIC_HOMEWORK = NETBASEURL + "index.php?g=apps&m=school&a=gethomeworknotice&schoolid=";

    /**
     * 五公开专栏（校本课程）
     */
    public static final String FIVE_PUBLIC_SUBJECT1 = NETBASEURL + "index.php?g=apps&m=school&a=getschoolsyl&schoolid=";

    /**
     * 五公开专栏（校本选修课程）
     */
    public static final String FIVE_PUBLIC_SUBJECT2 = NETBASEURL + "index.php?g=apps&m=school&a=getschoolsylchoice&schoolid=";

    /**
     * 五公开专栏（课时）
     */
    public static final String FIVE_PUBLIC_SUBJECT_TIME = NETBASEURL + "index.php?g=apps&m=school&a=getclasstime&schoolid=";

    /**
     * 五公开专栏（期末检测）
     */
    public static final String FIVE_PUBLIC_END = NETBASEURL + "index.php?g=apps&m=school&a=getendtest&schoolid=";

    /**
     * 五公开专栏（节假日）
     */
    public static final String FIVE_PUBLIC_HOLIDAY = NETBASEURL + "index.php?g=apps&m=school&a=getholiday&schoolid=";

    /**
     * 获取在线留言
     */
    public static final String GET_ONLINE_COMMENT = NETBASEURL + "index.php?g=apps&m=index&a=GetServiceFeed&userid=";
    /**
     * 获取家长所在班级的老师
     */
    public static final String GET_MTCLASS_TEACHER = NETBASEURL + "index.php?g=apps&m=teacher&a=getTeacher&classid=";

    /**
     * 检查链接
     */
    public static final String CHECK_VERSION = NETBASEURL + "index.php?g=apps&m=index&a=CheckVersion&type=android&versionid=";

    /**
     * 获取轮播图
     */
    public static final String GET_SLIDER_URL = NETBASEURL + "index.php?g=apps&m=index&a=GetRotatePic&schoolid=";

    /**
     * 获取老师对应的班级
     */
    public static final String TC_GET_CLASS = NETBASEURL + "index.php?g=apps&m=teacher&a=TeacherGetClass";

    /**
     * 学校消息:
     */
    public static final String SCHOOL_MNEWS_DELET = NETBASEURL + "index.php?g=apps&m=message&a=delete&id=";

    /**
     * 班级消息
     */
    public static final String CLASS_NEWS_DELET = NETBASEURL + "index.php?g=apps&m=student&a=delete_homework&id=";

    /**
     * 学校公告
     */
    public static final String SCHOOL_ANNOUNCE_DELET = NETBASEURL + "index.php?g=apps&m=school&a=delete_notice&id=";

    /**
     * 动态
     */
    public static final String TREND_DELET = NETBASEURL + "index.php?g=apps&m=index&a=delete_mic&id=";

    public static final String VIDEO_URL = NETBASEURL + "data/upload/video/";
    /**
     * 发送签到
     */
    public static final String CLASS_RESIGN = NETBASEURL + "index.php?g=apps&m=index&a=resign";
    public static final String GET_STUDENT_LIST_ATTENDENT = NETBASEURL + "index.php?g=apps&m=teacher&a=GetStudentAttendanceList";
    //http://dezhi.xiaocool.net/index.php?g=apps&m=teacher&a=ParentGetStudentAttendanceList&parented=&sign_date=
    public static final String ParentGetStudentAttendanceList = NETBASEURL + "index.php?g=apps&m=teacher&a=ParentGetStudentAttendanceList";
    public static final String TeacherGetStudentAttendanceList = NETBASEURL + "index.php?g=apps&m=teacher&a=GetStudentAttendanceList";

    /**
     * 【教师】销假同意、驳回接口
     *参数：
     applyid
     teacherid
     feedback
     status（-1不同意，1同意
     */
    public static final String REPLY_LEAVE_BACK = NETBASEURL+"index.php?g=apps&m=teacher&a=ReplyLeaveBack";
    /**
     * 获取审核权限老师列表
     */
    public static final String GET_LEAVERMANAGERLIST =NETBASEURL+"index.php?g=apps&m=school&a=GetLeaverManagerListByClassid";
    /**
     * 获取假条信息
     */
    public static final String GET_LEAVE_INFO=NETBASEURL+"index.php?g=apps&m=teacher&a=GetLeaveInfoByLeaveId";
    /**
     * 销假接口
     */
    public static final String APPLY_FOR_BACK_LEAVE = NETBASEURL+"index.php?g=apps&m=teacher&a=ApplyForBackLeave";
    /**
     * 同意驳回请假
     */
    public static final String REPLY_LEAVE = NETBASEURL+"index.php?g=apps&m=teacher&a=replyleave";
    /**
     *撤回销假接口
     */
    public static final String WITH_DRAWAL_LEAVE_BACK = NETBASEURL+"index.php?g=apps&m=student&a=WithdrawalLeaveBack";
    /**
     * 在线请假
     */
    public static final String SEND_LEAVE = NETBASEURL + "index.php?g=apps&m=index&a=LeaveTime";
    /**
     * 老师获取请假学生
     */

    public static final String CLASS_QINGJIA = NETBASEURL + "index.php?g=apps&m=teacher&a=getleavelist";
    public static final String STUDENT_QINGJIA = NETBASEURL + "index.php?g=apps&m=student&a=getleavelist";
    public static final String ParentGetFaceInfoWithLocation = NETBASEURL + "index.php?g=apps&m=student&a=ParentGetFaceInfoWithLocation";
    public static final String TeacherGetFaceInfoWithLocation = NETBASEURL + "index.php?g=apps&m=teacher&a=TeacherGetFaceInfoWithLocation";
    public static final String GetClassStudentAttendanceList = NETBASEURL + "index.php?g=apps&m=teacher&a=GetClassStudentAttendanceList";
    public static final String ParentGetExam = NETBASEURL + "index.php?g=apps&m=student&a=ParentGetExam";
    public static final String TeacherGetExam = NETBASEURL + "index.php?g=apps&m=teacher&a=TeacherGetExam";
    public static final String TeacherGetPerformance = NETBASEURL + "index.php?g=apps&m=teacher&a=TeacherGetPerformance";
    public static final String ParentGetPerformance = NETBASEURL + "index.php?g=apps&m=student&a=ParentGetPerformance";
    /**
     *创建会议
     */
    public static final String CREATE_CONFERENCE = NETBASEURL+"index.php?g=apps&m=Conference&a=createconference";
    /**
     *获取会议列表
     */
    public static final String GET_CONFERENCE_LIST_BY_SCHOOL_ID = NETBASEURL+"index.php?g=apps&m=Conference&a=getConferenceListBySchoolid";
    /**
     * 获取会议室列表
     */
    public static final String GET_MEETINGROOM_LIST= NETBASEURL+"index.php?g=apps&m=Conference&a=getMeetingroomList";
    /**
     * 获取单个会议信息
     */
    public static final String GET_CONFERENCE_BY_ID = NETBASEURL+"index.php?g=apps&m=Conference&a=getConferenceByid";
    /**
     * 我管理的会议列表
     */
    public static final String GET_MY_MANAGE_CONFERENCE_LIST_BY_USER_ID = NETBASEURL+"index.php?g=apps&m=Conference&a=getMyManageConferenceListByUserid";
    /**
     * 获取会议参会人员，和主持人 -新
     */
    public static final  String GET_MEETING_LIST=NETBASEURL+"index.php?g=apps&m=Conference&a=getConferencePeopleList";
    /**
     * 获取宿舍楼列表
     */
    public static final String GET_DORMITORY_LIST = NETBASEURL+"index.php?g=apps&m=teacher&a=dormitory_list";
    /**
     * 获取宿舍考勤统计
     */
    public static final String GET_DORMITORY_COUNT = NETBASEURL+"index.php?g=apps&m=teacher&a=dormitory_count";
    /**
     * 补签
     */
    public static final String TODAY_RETROACTIVE = NETBASEURL+"index.php?g=apps&m=teacher&a=today_retroactive";
    /**
     * 获取缺勤人员列表 入参：宿舍楼id（dormid）
     */
    public static final String GET_DORIMITORY_CHECK = NETBASEURL+"index.php?g=apps&m=teacher&a=dormitory_check";
    /**
     * 获取宿舍列表
     *
     *
     */
    public static final String GET_DORMITORY_ROOM_LIST = NETBASEURL+"index.php?g=apps&m=teacher&a=dormitory_roomlist";

    /**
     * 根据宿舍号获取考勤记录
     * http://dezhi.xiaocool.net/index.php?g=apps&m=teacher&a=dormitory_check&dormid=12&roomid=214&select_time=1517414400
     */
    public static final String GET_DORMITORY_CHECK =  NETBASEURL+"index.php?g=apps&m=teacher&a=dormitory_check";
    /**
     * 获取教师列表
     */
    public static final String GET_CLASS_TEACHER_LIST=NETBASEURL+"index.php?g=apps&m=school&a=getclassteacherlist";

    /**
     * 判断是否异地登录
     * "userid":userid,
     "registrationID":registrationID
     */
    public static final String CHECK_LOGIN_FROM_OTHER = NETBASEURL+"index.php?g=apps&m=index&a=CheckLoginFormOther";
    /**
     * 获取我的亲属  http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetInviteFamily&studentid=597
     */
    public static final String GET_INVITE_FAMILY = NETBASEURL+"index.php?g=apps&m=student&a=GetInviteFamily";
    /**
     * 添加亲属
     */
    public static final String ADD_INVITE_FAMILY = NETBASEURL+"index.php?g=apps&m=student&a=AddInviteFamily";
    /**
     * 获取可用修改亲属次数
     */
    public static final String GET_AVALIABLE_RELATION_COUNT = NETBASEURL+"index.php?g=apps&m=student&a=getAvailableRelationCount";
    /**
     * 设置第一联系人
     */
    public static final String SET_MAIN_PARENT = NETBASEURL+"index.php?g=apps&m=student&a=SetMainParent";
    /**
     * 删除亲属关系
     */
    public static final String DELETE_FAMILY = NETBASEURL+"index.php?g=apps&m=student&a=DeleteFamily";

}
