package com.example.trdemoapi.service;

import com.example.trdemoapi.dto.CreateSubjectReq;
import com.example.trdemoapi.dto.UpdateSubjectReq;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> allSubjects() {
        return subjectRepository.findAll();
    }

    public Subject loadSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Subject not found."));
    }

    @Transactional
    public Subject createSubject(CreateSubjectReq request) {
        var subject = new Subject()
                .setName(request.getName())
                .setType(request.getType().name())
                .setCredit(request.getCredit());

        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteSubject(Subject subject) {
        subjectRepository.delete(subject);
    }

    @Transactional
    public Subject updateSubject(Long id, UpdateSubjectReq request) {
        var subject = subjectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Subject not found."));

        if (request.getName() != null) subject.setName(request.getName());
        if (request.getCredit() != null) subject.setCredit(request.getCredit());
        if (request.getType() != null) subject.setType(request.getType().name());

        return subjectRepository.save(subject);
    }
}
