package br.edu.ifsp.scl.dicesdm;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BingoGridAdapter extends BaseAdapter {
    private int maxValue;
    private Set<Integer> selectedNumbers = new HashSet<>(maxValue);
    private List<List<String>> adaptedValueColumns = new ArrayList<>(Column.values().length);

    public BingoGridAdapter(int maxValue) {
        this.maxValue = maxValue;
        updateAdaptedValueColumns();
    }

    public boolean alreadySelectedNumber(Integer number) {
        return selectedNumbers.contains(number);
    }

    public boolean isFilled() {
        return selectedNumbers.size() == maxValue;
    }

    public void selectNumber(Integer number) {
        selectedNumbers.add(number);
        updateAdaptedValueColumns();
        notifyDataSetChanged();
    }

    public void clearSelectedNumbers() {
        selectedNumbers.clear();
        updateAdaptedValueColumns();
        notifyDataSetChanged();
    }

    private void updateAdaptedValueColumns() {
        adaptedValueColumns.clear();
        for (Column column : Column.values()) {
            List<Integer> values = column.containedValuesFrom(selectedNumbers, maxValue);
            List<String> columnValues = new ArrayList<>(values.size() + 1);
            columnValues.add(column.name());
            for (Integer value : values) {
                columnValues.add(value.toString());
            }
            adaptedValueColumns.add(columnValues);
        }
    }

    @Override
    public int getCount() {
        int greatestListSize = 0;
        for (List valueColumn : adaptedValueColumns) {
            greatestListSize = Math.max(greatestListSize, valueColumn.size());
        }
        return greatestListSize * adaptedValueColumns.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.bingo_grid_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(valueAtPosition(position));
        if (isFirstRow(position)) textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
        else textView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);

        return convertView;
    }

    private boolean isFirstRow(int position) {
        int rowIndex = position / Column.values().length;
        return rowIndex == 0;
    }

    private String valueAtPosition(int position) {
        int columnIndex = position % Column.values().length;
        int rowIndex = position / Column.values().length;
        List<String> columnRows = adaptedValueColumns.get(columnIndex);
        return columnRows.size() > rowIndex ? columnRows.get(rowIndex) : "";
    }

    private enum Column {
        B, I, N, G, O;

        List<Integer> containedValuesFrom(Set<Integer> values, int maxValue) {
            ArrayList<Integer> filteredValues = new ArrayList<>();
            for (Integer value : values) {
                if (isFromThis(value, maxValue)) filteredValues.add(value);
            }
            return filteredValues;
        }

        private boolean isFromThis(Integer value, int maxValue) {
            return columnOfValue(value, maxValue) == ordinal();
        }

        int columnOfValue(int value, int maxValue) {
            return (value - 1) / valuesPerColumn(maxValue);
        }

        int valuesPerColumn(int maxValue) {
            return maxValue / values().length;
        }
    }
}
