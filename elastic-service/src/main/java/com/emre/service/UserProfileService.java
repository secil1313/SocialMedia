package com.emre.service;

import com.emre.mapper.IElasticMapper;
import com.emre.rabbitmq.model.RegisterElasticModel;
import com.emre.repository.IUserProfileRepository;
import com.emre.repository.entity.UserProfile;
import com.emre.utility.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository iUserProfileRepository;

    public UserProfileService(ElasticsearchRepository<UserProfile, String> repository,
                              IUserProfileRepository iUserProfileRepository) {
        super(repository);
        this.iUserProfileRepository = iUserProfileRepository;
    }

    public UserProfile createUserWithRabbitMq(RegisterElasticModel registerElasticModel) {
        return save(IElasticMapper.INSTANCE.fromElasticToUserProfile(registerElasticModel));
    }

    /**
     * ElasticSearch de metinsel(string vs) değerler üzerinde herhangi bir sıralama yapılamamaktadır.
     * Bunun için elasticsearch arayüzüne kibana vb araçlarla ulaşarak bazı index ayarlarının yapılması gerekmektedir.
     */
    public Page<UserProfile> findAll(int currentPage, int size, String sortParameter, String sortDirection) {
        Sort sort = null;
        Pageable pageable = null;
        if (sortParameter != null)
            sort = Sort.by(Sort.Direction.fromString(sortDirection == null ? "ASC" : sortDirection), sortParameter); //Hangi parametreyi neye göre sıralayacağını söylüyoruz.
        if (sort != null)
            pageable = PageRequest.of(currentPage, size == 0 ? 10 : size, sort);
        else
            pageable = PageRequest.of(currentPage, size == 0 ? 10 : size);
        return iUserProfileRepository.findAll(pageable);
    }
}
