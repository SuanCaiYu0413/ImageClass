CREATE TABLE `Pictures` (
`PictureID` int UNSIGNED NOT NULL AUTO_INCREMENT,
`PictureName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
`Size` int NULL,
`Width` int NULL,
`Height` int NULL,
`Type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
PRIMARY KEY (`PictureID`) 
);

CREATE TABLE `Tag` (
`LabelID` int UNSIGNED NOT NULL,
`Name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名',
`PerantID` int NULL,
PRIMARY KEY (`LabelID`) 
);

CREATE TABLE `Users` (
`UserID` int UNSIGNED NOT NULL AUTO_INCREMENT,
`UserName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '就像QQ昵称一样',
`PassWord` varchar(64) NULL,
`UserSid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '就像QQ号码一样',
PRIMARY KEY (`UserID`) 
);

CREATE TABLE `ImgEdit` (
`ID` int UNSIGNED NOT NULL AUTO_INCREMENT,
`PictureID` int NOT NULL,
`LabelID` int NOT NULL,
`UserID` int NOT NULL,
`IsValid` int NOT NULL DEFAULT 0 COMMENT '是否奏效',
`CreateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
PRIMARY KEY (`ID`) 
);


ALTER TABLE `Pictures` ADD CONSTRAINT `fk_Pictures_ImgEdit_1` FOREIGN KEY (`PictureID`) REFERENCES `ImgEdit` (`PictureID`);
ALTER TABLE `Users` ADD CONSTRAINT `fk_Users_ImgEdit_1` FOREIGN KEY (`UserID`) REFERENCES `ImgEdit` (`UserID`);
ALTER TABLE `Tag` ADD CONSTRAINT `fk_Tag_ImgEdit_1` FOREIGN KEY (`LabelID`) REFERENCES `ImgEdit` (`LabelID`);

