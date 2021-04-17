<template>
  <div class="q-gutter-md">
    <div
      class="text-h5"
    >
      Authors
    </div>
    <q-separator :value="10"/>

    <div class="row justify-end q-gutter-x-sm">
      <q-btn
        no-caps
        class="bg-positive text-white"
        @click="addAuthorDialog.show = true"
      >
        New author
      </q-btn>
      <q-btn
        no-caps
        class="bg-positive text-white"
      >
        Save authors
      </q-btn>
    </div>
    <draggable
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
    <!--<q-dialog
      v-model="addAuthorDialog.show"
      @hide="clickAddAuthorAbort"
    >
      <q-card
        style="width: 400px"
      >
        <q-card-section
          class="q-gutter-md"
        >
          <div
            class="text-center text-h6"
          >
            New Author
          </div>
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
          <div class="row justify-evenly">
            <q-btn
              color="positive"
              label="Add"
              no-caps
              :disable="$v.addAuthorDialog.$invalid"
              @click="clickAddAuthor"
            />
            <q-btn
              color="negative"
              label="Abort"
              no-caps
              @click="clickAddAuthorAbort"
            />
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>-->
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
      addAuthorDialog: {
        show: false,
        name: ""
      },
      authors: [],
      allLogAuthors: []
    }
  },
  methods: {
    clickAddAuthor() {
      this.authors.push({
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
    }
  },
  created() {
    ProjectService.getLogAuthors(this.$route.params.id)
      .then(data => this.allLogAuthors = data);
    ProjectService.getAuthors(this.$route.params.id)
      .then(data => this.authors = data);
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
