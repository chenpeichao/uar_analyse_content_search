CREATE TABLE `es_item_column` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `columnname` varchar(255) DEFAULT NULL COMMENT '栏目名称',
   `columntag` int(11) DEFAULT NULL COMMENT '栏目标识',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

CREATE TABLE `es_item_static` (
   `id` int(10) NOT NULL AUTO_INCREMENT,
   `app_at` varchar(48) NOT NULL COMMENT '应用标识',
   `app_name` varchar(255) DEFAULT NULL COMMENT '应用名称',
   `title` varchar(255) NOT NULL COMMENT '文章标题',
   `url` text NOT NULL COMMENT '文章url',
   `pv` int(11) DEFAULT NULL COMMENT '浏览量pv',
   `uv` int(11) DEFAULT NULL COMMENT '访问量uv',
   `visit_time` double(11,2) DEFAULT NULL COMMENT '平均访问时长',
   `count_time` datetime DEFAULT NULL COMMENT '统计时间',
   `publish_time` datetime DEFAULT NULL COMMENT '文章发布时间',
   `column_name` varchar(255) DEFAULT NULL COMMENT '发布来源/媒体',
   `column_id` int(11) DEFAULT NULL COMMENT '栏目id',
   `static_item_id` varchar(255) NOT NULL COMMENT '统计库item_id',
   `caiyun_item_id` varchar(255) DEFAULT NULL COMMENT '内容库item_id',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=72484 DEFAULT CHARSET=utf8;