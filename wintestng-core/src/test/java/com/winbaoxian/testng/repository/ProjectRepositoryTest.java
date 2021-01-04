package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.BaseTest;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-26 15:23
 */
public class ProjectRepositoryTest extends BaseTest {

    @Resource
    private ProjectRepository projectRepository;

    @Test
    public void testSave() {
        ProjectEntity entity = new ProjectEntity();
        entity.setName("ddddd");
        projectRepository.save(entity);
    }
}
