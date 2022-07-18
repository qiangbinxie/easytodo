package com.mucong.easytodo.bean;

import com.mucong.easytodo.constant.TaskStateEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date createAt;
    private Date updateAt;
    private TaskStateEnum taskState;
}
