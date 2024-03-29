<template>
  <div class="q-gutter-y-md">
    <div
      class="text-h5"
    >
      Authors
    </div>
    <q-separator :value="10"/>

    <div class="row justify-end q-gutter-x-sm">
      <q-btn
        no-caps
        :disable="loadingAuthors"
        class="bg-positive text-white"
        @click="addAuthorDialog.show = true"
      >
        New author
      </q-btn>
      <q-btn
        no-caps
        :disable="loadingAuthors"
        :loading="savingAuthors"
        class="bg-positive text-white"
        @click="clickSaveAuthorSettings"
      >
        Save authors
      </q-btn>
    </div>
    <draggable
      v-if="!loadingAuthors"
      :list="unassociatedLogAuthors"
      group="authors"
      draggable=".item"
      :animation="200"
      class="rounded-borders q-list q-list--bordered q-list--separator"
    >
      <q-item
        slot="header"
        dense
        style="border-radius: 3px 3px 0 0"
        class="bg-red-3"
      >
        <q-item-section>
          <q-item-label overline>Unassociated Log-Authors</q-item-label>
        </q-item-section>
      </q-item>
      <q-item v-if="unassociatedLogAuthors.length === 0">
        <q-item-section
          class="text-center"
        >
          All authors assigned!
        </q-item-section>
      </q-item>
      <q-item
        v-else
        v-for="logAuthor of unassociatedLogAuthors"
        class="item"
        style="cursor: move"
      >
        <q-item-section
          avatar
          style="min-width: 0"
        >
          <q-icon size="xs" name="drag_indicator" />
        </q-item-section>
        <q-item-section>
          <q-item-label>{{ logAuthor.name }}</q-item-label>
        </q-item-section>
      </q-item>
    </draggable>
    <draggable
      v-if="!loadingAuthors"
      v-for="author of authors"
      :list="author.logAuthors"
      group="authors"
      draggable=".item"
      :animation="200"
      class="rounded-borders q-list q-list--bordered q-list--separator"
    >
      <q-item
        slot="header"
        dense
        style="border-radius: 3px 3px 0 0"
        class="bg-blue-2"
      >
        <q-item-section>
          <q-item-label overline>Author: {{ author.name }}</q-item-label>
        </q-item-section>
        <q-item-section side>
          <q-btn
            icon="delete"
            dense
            flat
            @click="clickDeleteAuthor(author)"
          />
        </q-item-section>
      </q-item>
      <q-item
        v-if="author.logAuthors.length === 0"
      >
        <q-item-section
          class="text-center"
        >
          No log-author assigned!
        </q-item-section>
      </q-item>
      <q-item
        v-else
        v-for="logAuthor of author.logAuthors"
        class="item"
        style="cursor: move"
      >
        <q-item-section
          avatar
          style="min-width: 0"
        >
          <q-icon size="xs" name="drag_indicator" />
        </q-item-section>
        <q-item-section>
          <q-item-label>{{ logAuthor.name }}</q-item-label>
        </q-item-section>
      </q-item>
    </draggable>
    <q-inner-loading
      v-if="loadingAuthors"
      showing
    >
      <q-spinner-pie
        size="lg"
      />
    </q-inner-loading>
    <c-question
      v-model="addAuthorDialog.show"
      question="New author"
      :disable-ok="$v.addAuthorDialog.$invalid"
      @clickOk="clickAddAuthor"
      @clickAbort="clickAddAuthorAbort"
      abort-color="red"
    >
      <q-form
        @submit.prevent="clickAddAuthor"
      >
        <q-input
          label="Name"
          v-model="addAuthorDialog.name"
          @input="$v.addAuthorDialog.name.$touch()"
          outlined
          :rules="[val => $v.addAuthorDialog.name.required || 'Required']"
        />
      </q-form>
    </c-question>
  </div>
</template>

<script>

import draggable from 'vuedraggable'
import {required} from "vuelidate/lib/validators";
import ProjectService from "src/service/ProjectService";
import CQuestion from "components/CQuestion";

export default {
  name: "ProjectSettingsAuthors",
  components: {CQuestion, draggable},
  data() {
    return {
      savingAuthors: false,
      loadingAuthors: false,
      addAuthorDialog: {
        show: false,
        name: ""
      },
      authors: [],
      allLogAuthors: []
    }
  },
  methods: {
    clickSaveAuthorSettings() {
      this.savingAuthors = true
      ProjectService.saveAuthorsAndLogAuthorGroups(this.$route.params.id,this.authors)
        .then(() => {
          this.$q.notify({
            type: 'positive',
            message: 'Authors updated'
          });
          this.loadingAuthors = true;
          this.fetchAndSetCurrentSettings()
            .finally(() => this.loadingAuthors = false);
        })
        .finally(() => {
          this.savingAuthors = false;
        })
    },
    clickAddAuthor() {
      this.authors.unshift({
        id: 0,
        name: this.addAuthorDialog.name,
        logAuthors: []
      });
      this.clickAddAuthorAbort();
    },
    clickDeleteAuthor(group) {
      this.authors = this.authors.filter(x => x !== group)
    },
    clickAddAuthorAbort() {
      this.addAuthorDialog.name = "";
      this.addAuthorDialog.show = false;
    },
    fetchAndSetCurrentSettings() {
      return new Promise(((resolve, reject) => {
        let successes = 0;
        ProjectService.getLogAuthors(this.$route.params.id)
          .then(data => {
            this.allLogAuthors = data;
            successes++;
            if(successes === 2) {
              resolve();
            }
          })
          .catch(err => reject(err));
        ProjectService.getAuthors(this.$route.params.id)
          .then(data =>{
            this.authors = data;
            successes++;
            if(successes === 2) {
              resolve();
            }
          })
          .catch(err => reject(err));
      }));
    }
  },
  created() {
    this.loadingAuthors = true;
    this.fetchAndSetCurrentSettings()
      .finally(() => this.loadingAuthors = false);
  },
  computed: {
    unassociatedLogAuthors() {
      let allAssignedLogAuthorIds = new Set();
      for(let author of this.authors) {
        for(let logAuthor of author.logAuthors) {
          allAssignedLogAuthorIds.add(logAuthor.id);
        }
      }
      return this.allLogAuthors.filter(x => !allAssignedLogAuthorIds.has(x.id))
    }
  },
  validations() {
    return {
      addAuthorDialog: {
        name: {
          required
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
