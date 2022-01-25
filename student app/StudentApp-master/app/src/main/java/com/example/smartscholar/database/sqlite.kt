package com.example.smartscholar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

/*Created By Divyanshu Gupta
This Activity is Created for All The Database Related Stuff
1) Creating Database
2) Creating Tables
3) Inserting data into Tables
4) Fetching data from Tables
*/

class sqlite(context: Context?) : SQLiteOpenHelper(context, sql_DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) //overriding the onCreate function for creating  Tables
    {
        db.execSQL("create table $sql_cohort (CH_id INTEGER ,CH_Name TEXT ,TC_id INTEGER ,CU_id INTEGER ,TP_id INTEGER ,CH_Semester INTEGER )")
        db.execSQL("create table $sql_course (CO_ID INTEGER ,CO_Name TEXT ,CO_Desc TEXT ,CO_Duration INTEGER ,CO_Image TEXT ,CO_Insertdate TEXT )")
        db.execSQL("create table $sql_content (CT_ID INTEGER ,CT_Name TEXT ,CT_Type TEXT ,CT_ContentLink TEXT ,CT_Duration INTEGER ,CT_Insertdate TEXT)")
        db.execSQL("create table $sql_concept (CN_id INTEGER ,CN_Name TEXT,CN_Desc TEXt, CN_Duration INTEGER, CN_Image TEXT, CN_Insertdate TEXT, CO_id INTEGER)")
        db.execSQL("create table $sql_subconcept (SC_id INTEGER, SC_Name TEXT ,SC_Desc TEXT, SC_Insertdate TEXT, SC_Duration INTEGER,CN_id INTEGER, CO_id INTEGER)")
        db.execSQL("create table $sql_sessionplan (SP_id INTEGER, SP_Name TEXT ,SP_Duration INTEGER, SP_Sequence INTEGER,CO_id INTEGER)")

        db.execSQL("create table $sql_sessionsection (SS_id INTEGER, SS_Content TEXT ,SS_ContentType TEXT, SS_Seqnum INTEGER, SS_Duration INTEGER,SP_id INTEGER,SC_id INTEGER,CO_id INTEGER,CT_id INTEGER)")
        db.execSQL("create table $sql_coursecontent (CO_id INTEGER, CN_id INTEGER,SC_id INTEGER,CT_id INTEGER)")
        db.execSQL("create table $sql_curriculum (CU_id INTEGER ,CU_Name TEXT ,CU_Desc TEXT ,CU_Image TEXT ,CU_Insertdate TEXT )")
        db.execSQL("create table $sql_curriculum_details (CU_id INTEGER, CO_id INTEGER,CO_SeqNo INTEGER,CO_Semester INTEGER,CO_Year INTEGER)")


    }//END OF onCreate Function

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) //overriding the onUpgrade function for droping  Tables
    {
        db.execSQL("DROP TABLE IF EXISTS $sql_cohort")
        db.execSQL("DROP TABLE IF EXISTS $sql_course")
        db.execSQL("DROP TABLE IF EXISTS $sql_content")
        db.execSQL("DROP TABLE IF EXISTS _$sql_concept")
        db.execSQL("DROP TABLE IF EXISTS _$sql_subconcept")
        db.execSQL("DROP TABLE IF EXISTS $sql_sessionplan")
        db.execSQL("DROP TABLE IF EXISTS $sql_sessionsection")
        db.execSQL("DROP TABLE IF EXISTS $sql_coursecontent")
        db.execSQL("DROP TABLE IF EXISTS $sql_curriculum")

        db.execSQL("DROP TABLE IF EXISTS $sql_curriculum_details")


        onCreate(db)
    }



    //function for inserting data into COHORT  Table
    fun insertData_into_Cohort(ch_id : Int?, ch_name: String?, tc_id : Int?, cu_id : Int?, tp_id : Int?, ch_semester : Int?): Boolean
    {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CH_id,  ch_id)
        l_contentValues.put(sql_CH_Name, ch_name)
        l_contentValues.put(sql_TC_id, tc_id)
        l_contentValues.put(sql_CU_id, cu_id)
        l_contentValues.put(sql_TP_id, tp_id)
        l_contentValues.put(sql_CH_Semester, ch_semester)

        val l_success1 = sql_db1.insert(sql_cohort, null, l_contentValues)
        return if (l_success1 == -1L) //if above statement is successfully completed with no error
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into COURSE  Table
    fun insertData_into_Course(co_id : Int?, co_name: String?, co_desc : String?, co_duration : Int?, co_image : String?, co_insertdate : String?): Boolean
{
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CO_id,  co_id)
        l_contentValues.put(sql_CO_Name, co_name)
        l_contentValues.put(sql_CO_Desc, co_desc)
        l_contentValues.put(sql_CO_Duration, co_duration)
        l_contentValues.put(sql_CO_Image, co_image)
        l_contentValues.put(sql_CO_Insertdate, co_insertdate)

        val l_success1 = sql_db1.insert(sql_course, null, l_contentValues)
        return if (l_success1 == -1L) //if above statement is successfully completed with no error
        {
            false
        } else {
            true
        }
    }

    fun insertData_into_Curriculum(cu_id : Int?, cu_name: String?, cu_desc : String?,cu_image : String?, cu_insertdate : String?): Boolean
    {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CU_Id,  cu_id)
        l_contentValues.put(sql_CU_Name, cu_name)
        l_contentValues.put(sql_CU_Desc, cu_desc)
        l_contentValues.put(sql_CU_Image, cu_image)
        l_contentValues.put(sql_CU_Insertdate, cu_insertdate)

        val l_success1 = sql_db1.insert(sql_curriculum, null, l_contentValues)
        return if (l_success1 == -1L) //if above statement is successfully completed with no error
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into CONTENT  Table
    fun insertData_into_Content(ct_id : Int?, ct_name: String?, ct_type : String?, ct_content_link : String?, ct_duration : Int?, ct_insertdate : String?): Boolean {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CT_id,  ct_id)
        l_contentValues.put(sql_CT_Name, ct_name)
        l_contentValues.put(sql_CT_Type, ct_type)
        l_contentValues.put(sql_CT_ContentLink, ct_content_link)
        l_contentValues.put(sql_CT_Duration, ct_duration)
        l_contentValues.put(sql_CT_Insertdate, ct_insertdate)


        val l_success1 = sql_db1.insert(sql_content, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into CONCEPT Table
    fun insertData_into_Concept(cn_id : Int?, cn_name: String?, cn_desc : String?, cn_duration : Int?, cn_image : String?, cn_insertdate : String?,co_id : Int?): Boolean {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CN_id,  cn_id)
        l_contentValues.put(sql_CN_Name, cn_name)
        l_contentValues.put(sql_CN_Desc, cn_desc)
        l_contentValues.put(sql_CN_Duration, cn_duration)
        l_contentValues.put(sql_CN_Image, cn_image)
        l_contentValues.put(sql_CN_Insertdate, cn_insertdate)
        l_contentValues.put(sql_CO_CN_ID, co_id)


        val l_success1 = sql_db1.insert(sql_concept, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into SUBCONCEPT Table
    fun insertData_into_SubConcept(sc_id : Int?, sc_name: String?, sc_desc : String?, sc_insertdate : String?,sc_duration : Int?,cn_id :  Int?,co_id : Int?): Boolean {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_SC_id,  sc_id)
        l_contentValues.put(sql_SC_Name, sc_name)
        l_contentValues.put(sql_SC_Desc, sc_desc)
        l_contentValues.put(sql_SC_Insertdate, sc_insertdate)
        l_contentValues.put(sql_SC_Duration, sc_duration)
        l_contentValues.put(sql_CN_SC_ID, cn_id)
        l_contentValues.put(sql_CO_SC_ID, co_id)

        val l_success1 = sql_db1.insert(sql_subconcept, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into SESSIONPLAN  Table
    fun insertData_into_SessionPlan(sp_id : Int?, sp_name: String?, sp_duration : Int?, sp_sequence : Int?,co_id : Int?): Boolean {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_SP_id,  sp_id)
        l_contentValues.put(sql_SP_Name, sp_name)
        l_contentValues.put(sql_SP_Duration, sp_duration)
        l_contentValues.put(sql_SP_Sequence, sp_sequence)
        l_contentValues.put(sql_CO_SP_ID, co_id)


        val l_success1 = sql_db1.insert(sql_sessionplan, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into SESSIONSECTION Table
    fun insertData_into_SessionSection(ss_id : Int?, ss_content: String?, ss_contenttype : String?, ss_seqnum : Int?,ss_duration : Int?,sp_id : Int?,sc_id : Int?,co_id : Int?,ct_id : Int?): Boolean
    {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_SS_id,  ss_id)
        l_contentValues.put(sql_SS_Content, ss_content)
        l_contentValues.put(sql_SS_ContentType, ss_contenttype)
        l_contentValues.put(sql_SS_Seqnum, ss_seqnum)
        l_contentValues.put(sql_SS_Duration, ss_duration)
        l_contentValues.put(sql_SP_SS_ID, sp_id)
        l_contentValues.put(sql_SC_SS_ID, sc_id)
        l_contentValues.put(sql_CO_SS_ID, co_id)
        l_contentValues.put(sql_CT_SS_ID, ct_id)

        val l_success1 = sql_db1.insert(sql_sessionsection, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    //function for inserting data into COURSECONTENT  Table
    fun insertData_into_CourseContent(co_id : Int?, cn_id : Int?, sc_id : Int?, ct_id : Int?): Boolean
    {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CO_CC_id,  co_id)
        l_contentValues.put(sql_CN_CC_id, cn_id)
        l_contentValues.put(sql_SC_CC_id, sc_id)
        l_contentValues.put(sql_CT_CC_id, ct_id)

        val l_success1 = sql_db1.insert(sql_coursecontent, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    fun insertData_into_CurriculumDetails(cu_id : Int?, co_id : Int?, co_seqno : Int?, co_semester : Int?, co_year : Int?): Boolean
    {
        val sql_db1 = this.writableDatabase
        val l_contentValues = ContentValues()
        l_contentValues.put(sql_CD_CU_id,  cu_id)
        l_contentValues.put(sql_CD_CO_id, co_id)
        l_contentValues.put(sql_CD_CO_SEQNO, co_seqno)
        l_contentValues.put(sql_CD_CO_SEMESTER, co_semester)
        l_contentValues.put(sql_CD_CO_YEAR, co_year)

        val l_success1 = sql_db1.insert(sql_curriculum_details, null, l_contentValues)
        return if (l_success1 == -1L)
        {
            false
        } else {
            true
        }
    }

    //feching all COHORTS from cohort table
    open fun getAllCohorts(): MutableList<String> {
        val Cohortlist: MutableList<String> = ArrayList()
        //get readable database
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT CH_Name FROM cohort", null)
        if (cursor.moveToFirst())//for fetching data from cursor
        {
            do {
                Cohortlist.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        //close the cursor
        cursor.close()
        //close the database
        db.close()
        return Cohortlist
    }

    //feching all COURSENAME from course table
    open fun getAllCourse(): MutableList<String> {
        val Courselist: MutableList<String> = ArrayList()
        //get readable database
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT CO_Name FROM course", null)
        if (cursor.moveToFirst()) //for fetching data from cursor
        {
            do {
                Courselist.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        //close the cursor
        cursor.close()
        //close the database
        db.close()
        return Courselist
    }

//FOR FETCHING LINK FROM THE CONTENT TABLE
fun getlink(ct_id: String): Cursor {
    val l_ct_id = '"'.toString() + ct_id + '"'
    val l_db1 = this.readableDatabase
    val l_strquery = "select $sql_CT_ContentLink from content where CT_id=$l_ct_id"
    return l_db1.rawQuery(l_strquery, null)
}


    //FOR FETCHING COURSEID FROM THE COURSE TABLE
    fun getCourseId(course: String): Cursor {
        val l_course = '"'.toString() + course + '"'
        val l_db1 = this.readableDatabase
        val l_strquery = "select CO_id from course where CO_Name=$l_course"
        return l_db1.rawQuery(l_strquery, null)
    }

    fun getCurriculumId(co_id: String): Cursor {
        val l_co_id = '"'.toString() + co_id + '"'
        val l_db1 = this.readableDatabase
        val l_strquery = "select CU_id from curriculumdetails where CU_Id=$l_co_id"
        return l_db1.rawQuery(l_strquery, null)
    }

    //FOR FETCHING SESSIONID FROM THE SESSIONPLAN TABLE
    fun getSessionId(co_id: String): Cursor {
        val l_co_id = '"'.toString() + co_id + '"'
        val l_db1 = this.readableDatabase
        val l_strquery = "select SP_id,SP_Name from sessionplan where CO_id=$l_co_id"
        return l_db1.rawQuery(l_strquery, null)
    }


    //FOR FETCHING CONCEPTID FROM THE CONCEPT TABLE
    fun getConceptId(co_id: String): Cursor {
        val l_co_id = '"'.toString() + co_id + '"'
        val l_db1 = this.readableDatabase
        val l_strquery = "select CN_id from concept where CO_id=$l_co_id"
        return l_db1.rawQuery(l_strquery, null)
    }



    //FOR FETCHING  SUBCONCEPTID FROM THE SUBCONCEPT TABLE
    fun getSubConceptId(cn_id: String,co_id: String?): Cursor {
        val l_cn_id = '"'.toString() + cn_id + '"'
        val l_co_id = '"'.toString() + co_id + '"'
        val l_db1 = this.readableDatabase
        val l_strquery = "select SC_id from subconcept where CN_id=$l_cn_id and CO_id=$l_co_id"
    return l_db1.rawQuery(l_strquery, null)
}

    //FOR FETCHING CONTENTID FROM THE CONTENT TABLE
    fun getContentId(co_id: String,cn_id: String?,sc_id: String?): Cursor {
        val l_co_id = '"'.toString() + co_id + '"'
        val l_cn_id = '"'.toString() + cn_id + '"'
        val l_sc_id = '"'.toString() + sc_id + '"'

        val l_db1 = this.readableDatabase
        val l_strquery = "select CT_id from coursecontent where CO_id=$l_co_id  and CN_id=$l_cn_id and SC_id=$l_sc_id"
        return l_db1.rawQuery(l_strquery, null)
    }

//fetching concept name from concept table for manage curriculum part
    fun displayConcept(CO_Id: Int): ArrayList<String> {
        val l_CN_Name = ArrayList<String>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from Concept where CO_id = $CO_Id", null)
        } catch (e: SQLiteException) {

        }

        var l_name: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {

                l_name = cursor.getString(1)

                l_CN_Name.add(l_name)
                cursor.moveToNext()
            }
        }
        return l_CN_Name;
    }

    //fetching sub concept name from subconcept table for manage curriculum part
    fun displaySubConcept(CN_Id: Int): ArrayList<String> {

        val l_SC_Name = ArrayList<String>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from SubConcept where CN_id = $CN_Id", null)
        } catch (e: SQLiteException) {

        }

        var l_name: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {

                l_name = cursor.getString(1)

                l_SC_Name.add(l_name)
                cursor.moveToNext()
            }
        }
        return l_SC_Name;

    }

    //fetching course name from course table for manage curriculum part
    fun displayCourse(): ArrayList<String> {
        val l_CO_Name = ArrayList<String>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from Course", null)
        } catch (e: SQLiteException) {

        }

        var l_name: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {

                l_name = cursor.getString(1)

                l_CO_Name.add(l_name)
                cursor.moveToNext()
            }
        }
        return l_CO_Name;

    }

    //fetching course id from course table for manage curriculum part
    fun getCourseID(name: String): String? {

        val db = readableDatabase
        var cursor: Cursor? = null
        try {

            cursor = db.rawQuery("select CO_id from Course" + " where CO_NAme =?", arrayOf(name))

        } catch (e: SQLiteException) {

        }

        var l_CO_Id: String? = null
        if (cursor != null) {
            cursor.moveToNext()
            l_CO_Id = cursor.getString(0)
        };
        return l_CO_Id
    }

    //fetching concept id from concept table for manage curriculum part
    fun getConceptID(name: String): String? {

        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from Concept" + " where CN_Name =?", arrayOf(name))
        } catch (e: SQLiteException) {

        }

        var l_CN_Id: String? = null
        if (cursor != null) {
            cursor.moveToNext()
            l_CN_Id = cursor.getString(0)
        };
        return l_CN_Id
    }
    //fetching sub concept id from concept table for manage curriculum part
    fun getsubConceptID(name: String): String? {

        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select SC_id from subconcept" + " where SC_Name =?", arrayOf(name))
        } catch (e: SQLiteException) {

        }

        var l_CN_Id: String? = null
        if (cursor != null) {
            cursor.moveToNext()
            l_CN_Id = cursor.getString(0)
        };
        return l_CN_Id
    }
    //fetching content name from content table for manage curriculum part
    fun displayContent(CO_Id: String,CN_Id: String,SC_Id: String): ArrayList<String> {

        val l_CONTENT = ArrayList<String>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select CT_id from coursecontent where CO_id = $CO_Id and CN_id= $CN_Id and SC_id =$SC_Id", null)
        } catch (e: SQLiteException) {

        }

        var l_name: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {

                l_name= getlinkContent(cursor.getString(0))


                l_CONTENT.add(l_name)
                cursor.moveToNext()
            }
        }
        return l_CONTENT;
        }

    //FOR FETCHING LINK FROM THE CONTENT TABLE
    fun getlinkContent(ct_id: String): String
    {
        var cursor: Cursor? = null
    val db = this.readableDatabase
        try {
            cursor = db.rawQuery("select CT_ContentLink from content where CT_id=$ct_id", null)
        } catch (e: SQLiteException) {

        }

        var l_Content: String? = null
        if (cursor != null) {
            cursor.moveToNext()
            l_Content = cursor.getString(0)
        }
        return l_Content!!

    }


    //fetching sub concept id from concept table for manage curriculum part
    fun getContentType(contentlink: String): String? {

        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select CT_Type from content" + " where CT_ContentLink =?", arrayOf(contentlink))
        } catch (e: SQLiteException) {

        }

        var l_CT_Type: String? = null
        if (cursor != null) {
            cursor.moveToNext()
            l_CT_Type = cursor.getString(0)
        }
        return l_CT_Type
    }

    //fetching sub concept id from concept table for manage curriculum part
    fun getContentId(contentlink: String): String? {

        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select CT_id from content" + " where CT_ContentLink =?", arrayOf(contentlink))
        } catch (e: SQLiteException) {

        }

        var l_CT_Id: String? = null
        if (cursor != null) {
            cursor.moveToNext()
            l_CT_Id = cursor.getString(0)
        }
        return l_CT_Id
    }


    fun updateContentTable(CT_DownloadLink: String?, CT_ID: String){
        val db = this.readableDatabase
        db.execSQL("UPDATE "+ sql_content+" SET $sql_CT_ContentLink = "+"'"+CT_DownloadLink+"' "+ "WHERE CT_ID = "+"'"+CT_ID+"'")
        }


    companion object {
        const val sql_DATABASE_NAME = "SMARTKAKSHA.db"
        const val sql_cohort="cohort"
        const val sql_CH_id = "CH_id"
        const val sql_CH_Name = "CH_Name"
        const val sql_TC_id = "TC_id"
        const val sql_CU_id = "CU_id"
        const val sql_TP_id = "TP_id"
        const val sql_CH_Semester = "CH_Semester"

        const val sql_course="course"
        const val sql_CO_id = "CO_id"
        const val sql_CO_Name = "CO_Name"
        const val sql_CO_Desc = "CO_Desc"
        const val sql_CO_Duration = "CO_Duration"
        const val sql_CO_Image = "CO_Image"
        const val sql_CO_Insertdate = "CO_Insertdate"

        const val sql_content="content"
        const val sql_CT_id = "CT_id"
        const val sql_CT_Name = "CT_Name"
        const val sql_CT_Type = "CT_Type"
        const val sql_CT_ContentLink = "CT_ContentLink"
        const val sql_CT_Duration = "CT_Duration"
        const val sql_CT_Insertdate = "CT_Insertdate"

        const val sql_concept="concept"
        const val sql_CN_id = "CN_id"
        const val sql_CN_Name = "CN_Name"
        const val sql_CN_Desc = "CN_Desc"
        const val sql_CN_Duration = "CN_Duration"
        const val sql_CN_Image = "CN_Image"
        const val sql_CN_Insertdate = "CN_Insertdate"
        const val sql_CO_CN_ID = "CO_id"

        const val sql_subconcept="subconcept"
        const val sql_SC_id = "SC_id"
        const val sql_SC_Name = "SC_Name"
        const val sql_SC_Desc = "SC_Desc"
        const val sql_SC_Insertdate = "SC_Insertdate"
        const val sql_SC_Duration = "SC_Duration"
        const val sql_CN_SC_ID = "CN_id"
        const val sql_CO_SC_ID = "CO_id"

        const val sql_sessionplan="sessionplan"
        const val sql_SP_id = "Sp_id"
        const val sql_SP_Name = "SP_Name"
        const val sql_SP_Duration = "SP_Duration"
        const val sql_SP_Sequence = "SP_Sequence"
        const val sql_CO_SP_ID = "CO_id"

        const val sql_sessionsection="sessionsection"
        const val sql_SS_id = "SS_id"
        const val sql_SS_Content = "SS_Content"
        const val sql_SS_ContentType = "SS_ContentType"
        const val sql_SS_Seqnum = "SS_Seqnum"
        const val sql_SS_Duration = "SS_Duration"
        const val sql_SP_SS_ID = "SP_id"
        const val sql_SC_SS_ID = "SC_id"
        const val sql_CO_SS_ID = "CO_id"
        const val sql_CT_SS_ID = "CT_id"

        const val sql_coursecontent="coursecontent"
        const val sql_CO_CC_id = "CO_id"
        const val sql_CN_CC_id = "CN_id"
        const val sql_SC_CC_id = "SC_id"
        const val sql_CT_CC_id = "CT_id"


        const val sql_curriculum="curriculum"
        const val sql_CU_Id = "CU_id"
        const val sql_CU_Name = "CU_Name"
        const val sql_CU_Desc = "CU_Desc"
        const val sql_CU_Image = "CU_Image"
        const val sql_CU_Insertdate = "CU_Insertdate"

        const val sql_curriculum_details="curriculumdetails"
        const val sql_CD_CU_id = "CU_id"
        const val sql_CD_CO_id = "CO_id"
        const val sql_CD_CO_SEQNO = "CO_SeqNo"
        const val sql_CD_CO_SEMESTER = "CO_Semester"
        const val sql_CD_CO_YEAR = "CO_Year"

    }

}

//END OF CLASS