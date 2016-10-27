<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class name="${table.tableName}" table="${table.tableName}" optimistic-lock="version">
		<#if fields?exists>
			<#list fields as field>
				<#if field.code == "id">
					<id name="id" type="java.lang.String" length="${field.length}" unsaved-value="null">
						<generator class="uuid" />
					</id>
				<#else>
					<property name="${field.code}"
						<#switch field.type>
							<#case "String">
								type="java.lang.String"
							<#break>
							<#case "Text">
								type="text"
							<#break>
							<#case "int">
								type="java.lang.Integer"
							<#break>
							<#case "double">
								<#if dataType=='MYSQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='ORACLE'>
									type="java.math.BigDecimal"
								<#elseif dataType=='POSTGRESQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='SQLSERVER'>
									type="java.math.BigDecimal"
								</#if>
							<#break>
							<#case "Date">
								<#if dataType=='MYSQL'>
									type="java.util.Date"
								<#elseif dataType=='ORACLE'>
									type="java.sql.Timestamp"
								<#elseif dataType=='POSTGRESQL'>
									type="java.util.Date"
								<#elseif dataType=='SQLSERVER'>
									type="java.util.Date"
								</#if>
							<#break>
							<#case "Datetime">
								<#if dataType=='MYSQL'>
									type="java.util.Date"
								<#elseif dataType=='ORACLE'>
									type="java.sql.Timestamp"
								<#elseif dataType=='POSTGRESQL'>
									type="java.util.Date"
								<#elseif dataType=='SQLSERVER'>
									type="java.util.Date"
								</#if>
							<#break>
							<#case "BigDecimal">
							  	<#if dataType=='MYSQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='ORACLE'>
									type="java.math.BigDecimal"
								<#elseif dataType=='POSTGRESQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='SQLSERVER'>
									type="java.math.BigDecimal"
								</#if>
							<#break>
							<#case "Blob">
								<#if dataType=='MYSQL'>
									type="blob"
								<#elseif dataType=='ORACLE'>
							 		type="blob"
								<#elseif dataType=='POSTGRESQL'>
									type="binary"
								<#elseif dataType=='SQLSERVER'>
									type="image"
								</#if>
							<#break>
						</#switch> access="property">
						<column name="${field.code}" <#if field.type=='double'||field.type=='BigDecimal'>
							precision="${field.length}" scale="${field.scale}"<#else><#if field.length??>length="${field.length}"</#if></#if>
							not-null="false"
							 unique="false">
							<comment>${field.name}</comment>
						</column>
					</property>
				</#if>
			</#list>
		</#if>
	</class>
</hibernate-mapping>