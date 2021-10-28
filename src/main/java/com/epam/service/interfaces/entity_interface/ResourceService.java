package com.epam.service.interfaces.entity_interface;
import com.epam.model.entity.Resource;

import java.util.List;

public interface ResourceService  {
    Long  addResource(Resource resource);
    Resource get(Long id);
    List<Resource> getAll();
    void deleteAll();
    boolean  ifExistsByCheckSum(String str);
}
