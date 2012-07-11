package com.cahoot.client.mock
import com.cahoot.client.services.CollabService
import com.cahoot.models.ShareModel
import com.cahoot.models.DocumentModel
import com.cahoot.models.PersonModel

class MockCollabService extends CollabService {
  override def share(doc: DocumentModel,
    collaborators: List[PersonModel]): ShareModel = {

    // TODO: Setup the sharing of this document

    new ShareModel(doc, collaborators)
  }

}