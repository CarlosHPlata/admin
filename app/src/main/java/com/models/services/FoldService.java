package com.models.services;

import android.content.Context;

import com.models.Fold;
import com.models.mappers.FoldMapper;

import java.util.ArrayList;

/**
 * Created by Edgar on 20/04/2015.
 */
public class FoldService {

    public FoldService(Context context){
        foldMapper = new FoldMapper(context);
    }

    public FoldService(){
    }

    public boolean addFold(Fold fold){
        long longAux = foldMapper.insertFold(fold);
        if(longAux > 0){
            return true;
        }else{
            return false;
        }
    }

    public void updateFold(Fold fold){
        foldMapper.updateFold(fold);
    }

    public ArrayList<Fold> findAllFoldsByNoteId(int noteId){
        return foldMapper.findAllByNoteId(noteId);
    }

    public void delete(Fold fold){
        foldMapper.deleteFold(fold);
    }

    private FoldMapper foldMapper;
}
