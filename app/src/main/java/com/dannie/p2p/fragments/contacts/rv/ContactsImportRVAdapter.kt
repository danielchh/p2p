package com.dannie.p2p.fragments.contacts.rv

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.dannie.p2p.R
import com.dannie.p2p.models.room.ContactRoom
import com.dannie.p2p.other.extensions.setImageUriPicasso
import kotlinx.android.synthetic.main.item_contact_import.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ContactsImportRVAdapter(private val items: ArrayList<ContactRoom>):
        RecyclerView.Adapter<ContactsImportRVAdapter.ViewHolder>() {

    private var itemsCheck: SparseBooleanArray = SparseBooleanArray(items.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact_import, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        with(holder.itemView){
            txtFirstName.text = currentItem.firstName
            txtLastName.text = currentItem.lastName ?: ""
            checkBox.isChecked = itemsCheck[position]
            if (currentItem.isAvatarAvailable){
                ivAvatar.setImageUriPicasso(currentItem.imageUri!!)
            } else {
                doAsync {
                    val drawable = ResourcesCompat.getDrawable(context.resources, currentItem.avatarId!!, null)
                    uiThread {
                        ivAvatar.setImageDrawable(drawable)
                    }
                }
            }
        }
        holder.itemView.setOnClickListener { changeCheckBoxState(it.checkBox, position) }
        holder.itemView.checkBox.setOnClickListener { changeCheckBoxState(it.checkBox, position) }
    }

    /**
     * Picasso doesn't support loading Android Vector Drawable from res/drawable folder,
     * so had to come up with my own solution
     * @param checkBox of which checked state should be toggled
     * @param position of item in Recyclerview
     */
    private fun changeCheckBoxState(checkBox: CheckBox, position: Int) {
        val newCheckedState = !itemsCheck[position]
        checkBox.isChecked = newCheckedState
        doAsync {
            itemsCheck.put(position, newCheckedState)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
}