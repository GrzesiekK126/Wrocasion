USE [WRO_DB]
GO
/****** Object:  Table [dbo].[Bundle]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Bundle](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[ready] [bit] NOT NULL CONSTRAINT [DF_Bundle_ready]  DEFAULT ((0)),
 CONSTRAINT [PK_Bundle] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Location]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Location](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[latitude] [float] NOT NULL,
	[longitude] [float] NOT NULL,
	[name] [nvarchar](200) NULL,
	[task_id] [int] NOT NULL,
 CONSTRAINT [PK_Location] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Resource]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Resource](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[path] [nvarchar](50) NOT NULL,
	[resource_type_id] [int] NOT NULL CONSTRAINT [DF_Resource_resource_type_id]  DEFAULT ((0)),
	[task_id] [int] NULL CONSTRAINT [DF_Resource_task_id]  DEFAULT ((2)),
 CONSTRAINT [PK_Resource] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[ResourceType]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ResourceType](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_ResourceType] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[SolvedByUser]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SolvedByUser](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[task_id] [int] NOT NULL,
	[user_id] [int] NOT NULL,
 CONSTRAINT [PK_SolvedByUser] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Task]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Task](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[task_name] [nvarchar](50) NOT NULL,
	[task_type_id] [int] NOT NULL,
	[bundle_id] [int] NOT NULL,
	[description_id] [int] NULL,
	[minimum_to_find] [int] NULL,
 CONSTRAINT [PK_Task] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[TaskType]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaskType](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[type_name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_TaskType] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[User]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[User](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[email] [varchar](50) NOT NULL,
	[password] [varchar](50) NOT NULL,
	[date_joined] [datetime] NOT NULL CONSTRAINT [DF_User_date_joined]  DEFAULT (getdate()),
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Visit]    Script Date: 2014-12-11 14:11:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Visit](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[location_id] [int] NOT NULL,
	[photo_path] [nvarchar](200) NOT NULL,
	[date] [datetime] NOT NULL CONSTRAINT [DF_Visit_date]  DEFAULT (getdate()),
 CONSTRAINT [PK_Visit] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[Location]  WITH CHECK ADD  CONSTRAINT [FK_Location_Task] FOREIGN KEY([task_id])
REFERENCES [dbo].[Task] ([id])
GO
ALTER TABLE [dbo].[Location] CHECK CONSTRAINT [FK_Location_Task]
GO
ALTER TABLE [dbo].[Resource]  WITH CHECK ADD  CONSTRAINT [FK_Resource_ResourceType] FOREIGN KEY([resource_type_id])
REFERENCES [dbo].[ResourceType] ([id])
GO
ALTER TABLE [dbo].[Resource] CHECK CONSTRAINT [FK_Resource_ResourceType]
GO
ALTER TABLE [dbo].[Resource]  WITH CHECK ADD  CONSTRAINT [FK_Resource_Task] FOREIGN KEY([task_id])
REFERENCES [dbo].[Task] ([id])
GO
ALTER TABLE [dbo].[Resource] CHECK CONSTRAINT [FK_Resource_Task]
GO
ALTER TABLE [dbo].[SolvedByUser]  WITH CHECK ADD  CONSTRAINT [FK_SolvedByUser_Task] FOREIGN KEY([task_id])
REFERENCES [dbo].[Task] ([id])
GO
ALTER TABLE [dbo].[SolvedByUser] CHECK CONSTRAINT [FK_SolvedByUser_Task]
GO
ALTER TABLE [dbo].[SolvedByUser]  WITH CHECK ADD  CONSTRAINT [FK_SolvedByUser_User] FOREIGN KEY([user_id])
REFERENCES [dbo].[User] ([id])
GO
ALTER TABLE [dbo].[SolvedByUser] CHECK CONSTRAINT [FK_SolvedByUser_User]
GO
ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FK_Task_Bundle] FOREIGN KEY([bundle_id])
REFERENCES [dbo].[Bundle] ([id])
GO
ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FK_Task_Bundle]
GO
ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FK_Task_Resource] FOREIGN KEY([description_id])
REFERENCES [dbo].[Resource] ([id])
GO
ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FK_Task_Resource]
GO
ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FK_Task_TaskType] FOREIGN KEY([task_type_id])
REFERENCES [dbo].[TaskType] ([id])
GO
ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FK_Task_TaskType]
GO
ALTER TABLE [dbo].[Visit]  WITH CHECK ADD  CONSTRAINT [FK_Visit_Location] FOREIGN KEY([location_id])
REFERENCES [dbo].[Location] ([id])
GO
ALTER TABLE [dbo].[Visit] CHECK CONSTRAINT [FK_Visit_Location]
GO
ALTER TABLE [dbo].[Visit]  WITH CHECK ADD  CONSTRAINT [FK_Visit_User] FOREIGN KEY([user_id])
REFERENCES [dbo].[User] ([id])
GO
ALTER TABLE [dbo].[Visit] CHECK CONSTRAINT [FK_Visit_User]
GO
