<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.xinchi.bean.mapper.${hbmMoudelVO.clzssName?cap_first}Mapper" >
  <resultMap id="BaseResultMap" type="com.xinchi.bean.${hbmMoudelVO.clzssName?cap_first}" >
    <#list hbmMoudelVO.columnList as columnVO>
	    <result column="${columnVO.propertyName}" property="${columnVO.propertyName}" jdbcType="${columnVO.jdbcType?upper_case}" />
	 </#list>  
	 	<result column="create_time" property="create_time" jdbcType="VARCHAR" />
 	    <result column="update_time" property="update_time" jdbcType="VARCHAR" />
  </resultMap>
  
  <sql id="Base_Column_List" >
  		
  	<#list hbmMoudelVO.columnList as columnVO>
    	${columnVO.propertyName}, 
    </#list>  
    	create_time,
    	update_time
  </sql>
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.String" >
    select 
    <include refid="Base_Column_List" />
    from ${hbmMoudelVO.tableName}
    where pk = ${r"#{pk,jdbcType=CHAR}"}
  </select>
  
  <delete id="deleteByPrimaryKey" parameterType="java.lang.String" >
    delete from ${hbmMoudelVO.tableName}
    where pk = ${r"#{pk,jdbcType=CHAR}"}
  </delete>
  
  <insert id="insert" parameterType="com.xinchi.bean.${hbmMoudelVO.clzssName?cap_first}" >
  	
    insert into ${hbmMoudelVO.tableName}
    
    <trim prefix="(" suffix=")" suffixOverrides=",">
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
        <#list hbmMoudelVO.columnList as columnVO>
    	<if test="${columnVO.propertyName} != null">
	        ${r"#"}{${columnVO.propertyName},jdbcType=${columnVO.jdbcType?upper_case}},
	      </if>
		 </#list>  
      <if test="create_time != null">
        ${r"#"}{create_time,jdbcType=VARCHAR},
      </if>
       <if test="update_time != null">
        ${r"#"}{update_time,jdbcType=VARCHAR},
      </if>
    </trim>		
  </insert>
  

  <update id="updateByPrimaryKey" parameterType="com.xinchi.bean.${hbmMoudelVO.clzssName?cap_first}" >
    update ${hbmMoudelVO.tableName}
    <set >
    	<#list hbmMoudelVO.columnList as columnVO>
    	<if test="${columnVO.propertyName} != null">
	        ${columnVO.propertyName} = ${r"#"}{${columnVO.propertyName},jdbcType=${columnVO.jdbcType?upper_case}},
	      </if>
		 </#list> 
      <if test="update_time != null">
        update_time = ${r"#"}{update_time,jdbcType=VARCHAR},
      </if>
    </set>
    where pk = ${r"#{pk,jdbcType=CHAR}"}
  </update>
 <select id="selectByParam" parameterType="com.xinchi.bean.${hbmMoudelVO.clzssName?cap_first}" resultMap="BaseResultMap">
   select 
    <include refid="Base_Column_List" />
    from ${hbmMoudelVO.tableName}
   </select>
</mapper>