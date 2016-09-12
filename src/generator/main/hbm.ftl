<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="mappers.${hbmMoudelVO.clzssName?cap_first}Mapper" >
  <resultMap id="BaseResultMap" type="${hbmMoudelVO.clzssName?cap_first}" >
    	<id column="id" property="id" jdbcType="CHAR" />
    <#list hbmMoudelVO.columnList as columnVO>
	    <result column="${columnVO.propertyName}" property="${columnVO.propertyName}" jdbcType="${columnVO.jdbcType?upper_case}" />
	 </#list>  
	 	<result column="create_time" property="create_time" jdbcType="TIMESTAMP" />
 	    <result column="update_time" property="update_time" jdbcType="TIMESTAMP" />
  </resultMap>
  
  <sql id="Base_Column_List" >
  		
  	<#list hbmMoudelVO.columnList as columnVO>
    	${columnVO.propertyName}, 
    </#list>  
    	create_time,update_time
  </sql>
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.String" >
    select 
    <include refid="Base_Column_List" />
    from ${hbmMoudelVO.tableName}
    where id = ${r"#{id,jdbcType=CHAR}"}
  </select>
  
  <delete id="deleteByPrimaryKey" parameterType="java.lang.String" >
    delete from ${hbmMoudelVO.tableName}
    where id = ${r"#{id,jdbcType=CHAR}"}
  </delete>
  
  <insert id="insert" parameterType="${hbmMoudelVO.clzssName?cap_first}" >
  	
    insert into ${hbmMoudelVO.tableName}
    
    <trim prefix="(" suffix=")" suffixOverrides=",">
    	<if test="id != null">
	        id,
	      </if>
    	<#list hbmMoudelVO.columnList as columnVO>
	 	<if test="${columnVO.propertyName} != null">
	        ${columnVO.propertyName},
	      </if>
		 </#list>  
      <if test="create_time != null">
        create_time,
      </if>
      <if test="update_time != null">
        update_time,
      </if>
    </trim>
    <trim prefix="values (" suffix=")" suffixOverrides=",">
        <if test="id != null">
       	 ${r"#{id},"}
        </if>
        <#list hbmMoudelVO.columnList as columnVO>
    	<if test="${columnVO.propertyName} != null">
	        ${r"#"}{${columnVO.propertyName},jdbcType=${columnVO.jdbcType?upper_case}},
	      </if>
		 </#list>  
      <if test="create_time != null">
        ${r"#"}{create_time,jdbcType=VARCHAR},
      </if>
       <if test="update_time != null">
        ${r"#"}{update_time,jdbcType=TIMESTAMP},
      </if>
    </trim>		
  </insert>
  

  <update id="updateByPrimaryKey" parameterType="${hbmMoudelVO.clzssName?cap_first}" >
    update ${hbmMoudelVO.tableName}
    <set >
    	<#list hbmMoudelVO.columnList as columnVO>
    	<if test="${columnVO.propertyName} != null">
	        ${columnVO.propertyName} = ${r"#"}{${columnVO.propertyName},jdbcType=${columnVO.jdbcType?upper_case}},
	      </if>
		 </#list> 
       <if test="modifier != null">
        modifier = ${r"#"}{modifier,jdbcType=VARCHAR},
      </if>
      <if test="modify_time != null">
        modify_time = ${r"#"}{modify_time,jdbcType=VARCHAR},
      </if>
    </set>
    where id = ${r"#{id,jdbcType=CHAR}"}
  </update>
 <select id="selectByParam" parameterType="${hbmMoudelVO.clzssName?cap_first}" resultMap="BaseResultMap">
   select 
    <include refid="Base_Column_List" />
    from ${hbmMoudelVO.tableName}
   <where>
	    <if test="user_id !=null">
	    	user_id = ${r"#"}{user_id,jdbcType=CHAR}
	    </if>
	    <if test="res_id !=null">
	    	and res_id = ${r"#"}{res_id,jdbcType=CHAR}
	    </if>
    </where>
   </select>
</mapper>