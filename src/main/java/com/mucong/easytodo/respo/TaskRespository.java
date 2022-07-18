package com.mucong.easytodo.respo;

import com.mucong.easytodo.bean.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface TaskRespository extends JpaRepository<Task, Long> {
}
