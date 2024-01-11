package com.example.chatdiary.service

import com.example.chatdiary.data.Diary
import com.example.chatdiary.ui.view.main.diary.DiaryRequest
import com.example.chatdiary.ui.view.main.diary.DayDiaryVo
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import com.rabbitmq.client.ConnectionFactory


class AMQPProducer {
    // 配置 RabbitMQ 服务器的连接信息
    private val factory = ConnectionFactory().apply {
        host = "124.70.150.192" // RabbitMQ 服务器地址
        port = 5672 // 默认端口
        username = "guest"
        password = "guest"
    }
    suspend fun sendDiary(diary: DiaryRequest) {

        val connection = factory.newConnection()

        val channel = connection.createChannel()

        val queueName = "diary_queue"
        channel.queueDeclare(queueName, false, false, false, null)
        // 将对象转为 JSON 字符串，发送消息到队列中
        val diaryJson = Gson().toJson(diary)
        channel.basicPublish("", queueName, null, diaryJson.toByteArray())
        // 关闭通道和连接
        channel.close()
        connection.close()
    }
}
interface DiaryService {
    @GET("/v1/diaries")
    suspend fun getDiaries(): CommonResponse<List<Diary>>



    @Multipart
    @POST("/v1/image")
    suspend fun uploadImage(
        @Part("type") type: String,
        @Part("position") position: String,
        @Part("content") content: String,
        @Part image: List<MultipartBody.Part>
    );

    @GET("/v1/gen/diaryDateList")
    suspend fun getDiaryGenList(
        @Query("number") number: Long,
    ): CommonResponse<List<DayDiaryVo>>;
}