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
        @click="newGroupDialog.show = true"
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
      v-for="authorGroup of authorGroups"
      :list="authorGroup.authors"
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
          <q-item-label overline>Author: {{ authorGroup.name }}</q-item-label>
        </q-item-section>
        <q-item-section side>
          <q-btn
            icon="delete"
            dense
            flat
            @click="clickGroupDelete(authorGroup)"
          />
        </q-item-section>
      </q-item>
      <q-item
        v-if="authorGroup.authors.length === 0"
      >
        <q-item-section
          class="text-center"
        >
          No log-author assigned!
        </q-item-section>
      </q-item>
      <q-item
        v-else
        v-for="author of authorGroup.authors"
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
          <q-item-label>{{ author.name }}</q-item-label>
        </q-item-section>
      </q-item>
    </draggable>

    <draggable
      :list="unassociatedAuthors"
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
          <q-item-label overline>Unassociated log-authors</q-item-label>
        </q-item-section>
      </q-item>
      <q-item v-if="unassociatedAuthors.length === 0">
        <q-item-section
          class="text-center"
        >
          All authors assigned!
        </q-item-section>
      </q-item>
      <q-item
        v-else
        v-for="author of unassociatedAuthors"
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
          <q-item-label>{{ author.name }}</q-item-label>
        </q-item-section>
      </q-item>
    </draggable>
    <q-dialog
      v-model="newGroupDialog.show"
      @hide="clickGroupAddAbort"
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
            @submit.prevent="clickGroupAdd"
          >
            <q-input
              label="Name"
              v-model="newGroupDialog.name"
              @input="$v.newGroupDialog.name.$touch()"
              outlined
              :rules="[val => $v.newGroupDialog.name.required || 'Required']"
            />
          </q-form>
        </q-card-section>
        <q-card-actions
          align="center"
        >
          <q-btn
            color="positive"
            label="Add"
            no-caps
            :disable="$v.newGroupDialog.$invalid"
            @click="clickGroupAdd"
          />
          <q-btn
            color="negative"
            label="Abort"
            no-caps
            @click="clickGroupAddAbort"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>

import draggable from 'vuedraggable'
import {required} from "vuelidate/lib/validators";
import ProjectService from "src/service/ProjectService";

export default {
  name: "ProjectSettingsAuthors",
  components: {draggable},
  data() {
    return {
      newGroupDialog: {
        show: false,
        name: ""
      },
      authorGroups: [
        {
          id: 1,
          name: "Gruppe 1",
          authors: [{
            id: 1,
            name: "DanielDobby"
          }, {
            id: 4,
            name: "Daniel Poslon"
          }]
        }, {
          id: 2,
          name: "Gruppe 2",
          authors: [{
            id: 2,
            name: "alex9849"
          }, {
            id: 3,
            name: "Alexander Liggesmeyer"
          }]
        }
      ],
      allAuthors: []
    }
  },
  methods: {
    clickGroupAdd() {
      this.authorGroups.push({
        id: 0,
        name: this.newGroupDialog.name,
        authors: []
      });
      this.clickGroupAddAbort();
    },
    clickGroupDelete(group) {
      this.authorGroups = this.authorGroups.filter(x => x !== group)
    },
    clickGroupAddAbort() {
      this.newGroupDialog.name = "";
      this.newGroupDialog.show = false;
    }
  },
  created() {
    ProjectService.getLogAuthors(this.$route.params.id)
      .then(data => this.allAuthors = data);
  },
  computed: {
    unassociatedAuthors() {
      let allAssignedAuthorIds = new Set();
      for(let authorGroup of this.authorGroups) {
        for(let author of authorGroup.authors) {
          allAssignedAuthorIds.add(author.id);
        }
      }
      return this.allAuthors.filter(x => !allAssignedAuthorIds.has(x.id))
    }
  },
  validations() {
    return {
      newGroupDialog: {
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
